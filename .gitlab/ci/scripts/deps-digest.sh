#!/usr/bin/env bash
# Weekly Discord notifier for merged Renovate dependency MRs.
#
# Renovate auto-merges some updates, so the maintainer never sees the
# release notes embedded in those MRs. This script runs on the
# `deps-digest` scheduled pipeline, collects MRs merged in the past 7
# days that carry the `dependencies` label, and posts ONE Discord embed
# per upgraded dep — so each release-notes block is clearly tied to the
# dep it belongs to. MRs without release notes (e.g. internal Renovate
# config tweaks that happen to carry the `dependencies` label) are
# silently skipped.

set -euo pipefail

: "${DISCORD_DEPS_WEBHOOK_URL:?DISCORD_DEPS_WEBHOOK_URL must be set (configure as a masked + protected CI/CD variable)}"
: "${CI_API_V4_URL:?CI_API_V4_URL must be set (GitLab CI built-in)}"
: "${CI_PROJECT_ID:?CI_PROJECT_ID must be set (GitLab CI built-in)}"
: "${CI_JOB_TOKEN:?CI_JOB_TOKEN must be set (GitLab CI built-in)}"

# 7-day rolling window. updated_after must be ISO 8601 in UTC.
window_start=$(date -u -d "7 days ago" +"%Y-%m-%dT%H:%M:%SZ")

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

mr_count=$(jq 'length' <<<"${mrs}")
echo "Found ${mr_count} merged dependency MR(s)."

if [ "${mr_count}" -eq 0 ]; then
  echo "Nothing to digest this week — skipping Discord post."
  exit 0
fi

# Discord embed limits (leave headroom below the hard caps).
title_max=250
desc_max=4000

post_dep_embed() {
  local dep_title="$1" mr_url="$2" mr_title="$3" body="$4"

  if [ "${#dep_title}" -gt "${title_max}" ]; then
    dep_title="${dep_title:0:${title_max}}…"
  fi
  if [ "${#body}" -gt "${desc_max}" ]; then
    body="${body:0:${desc_max}}"$'\n…(truncated — see GitLab for the full notes)'
  fi

  local payload
  payload=$(jq -n \
    --arg title  "${dep_title}" \
    --arg url    "${mr_url}" \
    --arg desc   "${body}" \
    --arg footer "from ${mr_title}" \
    '{
       embeds: [{
         title:       $title,
         url:         $url,
         description: $desc,
         color:       5814783,
         footer:      { text: $footer }
       }]
     }')

  curl --silent --fail --show-error \
    --request POST \
    --header "Content-Type: application/json" \
    --data "${payload}" \
    "${DISCORD_DEPS_WEBHOOK_URL}" >/dev/null

  # Stay well below Discord's webhook rate limit (5 messages / 2s).
  sleep 0.5
}

# awk parser: emits one record per <details> block found inside each MR
# description's "### Release Notes" section. Output is a sequence of
# DEP-START / SUMMARY: / VERSION: / BODY: / <body lines> / DEP-END lines
# that the surrounding bash loop consumes as a state machine.
parse_dep_blocks_script='
  /^### Release Notes/  { in_rn = 1; next }
  /^### Configuration/  { in_rn = 0 }
  !in_rn                { next }

  /<details>/ {
    in_details = 1
    summary    = ""
    body       = ""
    next
  }

  /<\/details>/ {
    if (in_details) {
      version = ""
      if (match(body, /### \[`[^`]+`\]/)) {
        full = substr(body, RSTART, RLENGTH)
        if (match(full, /`[^`]+`/)) {
          version = substr(full, RSTART + 1, RLENGTH - 2)
        }
      }
      print "DEP-START"
      print "SUMMARY:" summary
      print "VERSION:" version
      print "BODY:"
      printf "%s", body
      print ""
      print "DEP-END"
      in_details = 0
    }
    next
  }

  in_details {
    line = $0
    if (summary == "" && match(line, /<summary>[^<]*<\/summary>/) > 0) {
      s = substr(line, RSTART, RLENGTH)
      sub(/^<summary>/, "", s)
      sub(/<\/summary>$/, "", s)
      summary = s
    } else {
      body = body line "\n"
    }
  }
'

posted=0
skipped_mrs=0

while IFS= read -r mr_b64; do
  mr=$(printf '%s' "${mr_b64}" | base64 --decode)
  mr_title=$(jq -r '.title' <<<"${mr}")
  mr_url=$(jq -r '.web_url' <<<"${mr}")
  description=$(jq -r '.description // ""' <<<"${mr}")

  state=outside
  summary=""
  version=""
  body=""
  mr_posted=0

  while IFS= read -r line; do
    case "${line}" in
      "DEP-START")
        state=meta
        summary=""
        version=""
        body=""
        ;;
      "DEP-END")
        body="${body%$'\n'}"
        if [ -n "${summary}" ]; then
          if [ -n "${version}" ]; then
            dep_title="${summary} — ${version}"
          else
            dep_title="${summary}"
          fi
          post_dep_embed "${dep_title}" "${mr_url}" "${mr_title}" "${body}"
          posted=$((posted + 1))
          mr_posted=$((mr_posted + 1))
          echo "Posted: ${dep_title} (from ${mr_title})"
        fi
        state=outside
        ;;
      "BODY:")
        state=body
        ;;
      *)
        case "${state}" in
          meta)
            if [[ "${line}" == SUMMARY:* ]]; then
              summary="${line#SUMMARY:}"
            elif [[ "${line}" == VERSION:* ]]; then
              version="${line#VERSION:}"
            fi
            ;;
          body)
            body+="${line}"$'\n'
            ;;
        esac
        ;;
    esac
  done < <(printf '%s\n' "${description}" | awk "${parse_dep_blocks_script}")

  if [ "${mr_posted}" -eq 0 ]; then
    skipped_mrs=$((skipped_mrs + 1))
    echo "Skipped (no release notes): ${mr_title}"
  fi
done < <(jq -r '.[] | @base64' <<<"${mrs}")

echo "Done. Posted ${posted} dep message(s); skipped ${skipped_mrs} MR(s) without release notes."
