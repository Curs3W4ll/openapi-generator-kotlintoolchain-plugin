# Weekly Dependency Digest

Renovate auto-merges patch and minor updates for the dependencies it
manages (see [`renovate.json5`](../renovate.json5) and !42). Because
those MRs merge without human review, the release notes Renovate embeds
in the MR body would otherwise go unseen.

The `deps-digest` scheduled job collects every MR merged in the past 7
days that carries the `dependencies` label, walks each MR's
**Release Notes** section, and posts **one Discord embed per upgraded
dep** via webhook — so each release-notes block stays tied to the dep it
belongs to (multi-dep group MRs like *Update mise tools* would otherwise
mash all their notes into one blob).

## Files

- [`.gitlab/ci/deps-digest.gitlab-ci.yml`](../.gitlab/ci/deps-digest.gitlab-ci.yml) — job
  definition
- [`.gitlab/ci/scripts/deps-digest.sh`](../.gitlab/ci/scripts/deps-digest.sh) — digest
  logic (GitLab REST query, release-notes extraction, Discord POST).

## Required CI/CD variables

| Variable                   | Required | How to obtain                                                                  |
|----------------------------|----------|--------------------------------------------------------------------------------|
| `DISCORD_DEPS_WEBHOOK_URL` | yes      | Discord channel *Edit Channel → Integrations → Webhooks → New Webhook*.        |

Configure `DISCORD_DEPS_WEBHOOK_URL` under **Settings → CI/CD → Variables**
as **masked** and **protected**. The job will fail loudly if the variable
is missing on a scheduled run.

## Behavior

- **One message per dep:** each `<details>` block found inside the MR's
  `### Release Notes` section becomes its own Discord embed. The embed
  title is the dep name (plus the new version when the script can parse
  it from the body), the title links back to the MR, and the MR title
  goes in the footer.
- **No merges in the window:** the job logs the empty result and exits 0
  without posting to Discord.
- **Long release notes:** Discord embed descriptions are capped at 4096
  characters. The script truncates at 4000 characters and appends a
  `…(truncated)` marker pointing the maintainer back to GitLab.
- **MRs without release notes** (e.g. internal Renovate config tweaks
  that happen to carry the `dependencies` label): silently skipped —
  there's nothing to surface.
- **Rate limit:** the script sleeps 0.5 s between POSTs to stay well
  below Discord's webhook rate limit (5 messages / 2 s).
- **Missing webhook URL:** the script exits non-zero with an explicit
  error referencing the variable name.
