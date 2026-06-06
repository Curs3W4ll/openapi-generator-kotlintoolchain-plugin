#!/usr/bin/env bash
# Weekly Discord digest of merged Renovate dependency MRs.
#
# Renovate auto-merges some updates, so the maintainer never sees the
# release notes embedded in those MRs. This script runs on the
# `deps-digest` scheduled pipeline, collects MRs merged in the past 7 days
# that carry the `dependencies` label, extracts each MR's Release Notes
# block, and posts a single concatenated markdown digest to a Discord
# channel via webhook.

set -euo pipefail

: "${DISCORD_DEPS_WEBHOOK_URL:?DISCORD_DEPS_WEBHOOK_URL must be set (configure as a masked + protected CI/CD variable)}"
: "${CI_API_V4_URL:?CI_API_V4_URL must be set (GitLab CI built-in)}"
: "${CI_PROJECT_ID:?CI_PROJECT_ID must be set (GitLab CI built-in)}"
: "${CI_JOB_TOKEN:?CI_JOB_TOKEN must be set (GitLab CI built-in)}"

# 7-day rolling window. updated_after must be ISO 8601 in UTC.
window_start=$(date -u -d "7 days ago" +"%Y-%m-%dT%H:%M:%SZ")
week_label=$(date -u +"%Y-%m-%d")

echo "Fetching dependency MRs merged since ${window_start}..."

mrs=$(curl --silent --fail --show-error \
  --header "JOB-TOKEN: ${CI_JOB_TOKEN}" \
  --get \
  --data-urlencode "state=merged" \
  --data-urlencode "labels=dependencies" \
  --data-urlencode "updated_after=${window_start}" \
  --data-urlencode "order_by=updated_at" \
  --data-urlencode "sort=desc" \
  --data-urlencode "per_page=100" \
  "${CI_API_V4_URL}/projects/${CI_PROJECT_ID}/merge_requests")

count=$(jq 'length' <<<"${mrs}")
echo "Found ${count} merged dependency MR(s)."

if [ "${count}" -eq 0 ]; then
  echo "Nothing to digest this week — skipping Discord post."
  exit 0
fi

# Build the markdown digest. Each MR contributes a heading (the MR title
# already contains "<dep> to <ver>" from Renovate's commit-message
# template) followed by the Release Notes block extracted from the MR
# description.
digest="## Dependency digest — week of ${week_label}"$'\n'

while IFS= read -r mr_b64; do
  mr=$(printf '%s' "${mr_b64}" | base64 --decode)
  title=$(jq -r '.title' <<<"${mr}")
  url=$(jq -r '.web_url' <<<"${mr}")
  description=$(jq -r '.description // ""' <<<"${mr}")

  # Extract content between Renovate's "### Release Notes" header and the
  # next top-level section ("### Configuration") or end-of-input. Strip
  # the wrapping <details>/<summary> markup so the message renders cleanly
  # in Discord.
  notes=$(printf '%s\n' "${description}" | awk '
    /^### Release Notes/  { in_block = 1; next }
    /^### Configuration/  { in_block = 0 }
    /^---$/               { in_block = 0 }
    in_block              { print }
  ' | sed -E -e 's#</?details>##g' -e 's#</?summary>##g')

  digest+=$'\n'"### [${title}](${url})"$'\n'
  if [ -n "$(printf '%s' "${notes}" | tr -d '[:space:]')" ]; then
    digest+="${notes}"$'\n'
  else
    digest+="_No release notes._"$'\n'
  fi
done < <(jq -r '.[] | @base64' <<<"${mrs}")

# Discord embed description limit is 4096 chars. Truncate with an
# explicit marker so the maintainer can tell the digest was cut.
max_len=4000
if [ "${#digest}" -gt "${max_len}" ]; then
  digest="${digest:0:${max_len}}"$'\n…(truncated — see GitLab for the full list)'
fi

payload=$(jq -n \
  --arg title "Dependency digest — week of ${week_label}" \
  --arg desc  "${digest}" \
  '{
     embeds: [{
       title:       $title,
       description: $desc,
       color:       5814783
     }]
   }')

echo "Posting digest to Discord webhook..."
curl --silent --fail --show-error \
  --request POST \
  --header "Content-Type: application/json" \
  --data "${payload}" \
  "${DISCORD_DEPS_WEBHOOK_URL}" >/dev/null

echo "Done."
