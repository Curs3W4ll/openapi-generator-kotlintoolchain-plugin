# Weekly Dependency Digest

Renovate auto-merges patch and minor updates for the dependencies it
manages (see [`renovate.json5`](../renovate.json5) and !42). Because
those MRs merge without human review, the release notes Renovate embeds
in the MR body would otherwise go unseen.

The `deps-digest` scheduled job collects every MR merged in the past 7
days that carries the `dependencies` label, extracts the
**Release Notes** block from each MR description, and posts a single
markdown digest to a Discord channel via webhook.

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

- **No merges in the window:** the job logs the empty result and exits 0
  without posting to Discord.
- **Long digest:** Discord embed descriptions are capped at 4096
  characters. The script truncates at 4000 characters and appends a
  `…(truncated)` marker pointing the maintainer back to GitLab.
- **Missing webhook URL:** the script exits non-zero with an explicit
  error referencing the variable name.
