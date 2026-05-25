# Release Process

This project ships releases through a manually triggered GitLab pipeline. The pipeline computes the next version,
generates release notes with [git-cliff](https://git-cliff.org/), and creates a GitLab release (the tag is created
by release-cli via the API). A GitHub workflow mirrors the tag into a GitHub release.

## Triggering a release

1. Open the project in GitLab and go to **Build → Pipelines → Run pipeline**.
2. Pick the default branch as the ref.
3. Add a pipeline variable `RELEASE` with value `true`.
4. Optional: add a `VERSION` variable (e.g. `1.2.3`) to override the auto-bumped version. This is not advised.
5. Run the pipeline.

The release pipeline runs `build` and `check` first, then `release-prepare` (writes `RELEASE_NOTES.md`) and finally
`release-publish` (creates the tag and the GitLab release through the API using `CI_JOB_TOKEN`).

## Authentication

No project access token is required. `release-cli` authenticates with the built-in `CI_JOB_TOKEN`, which is allowed
to create tags and releases through the GitLab API.

## Version computation

By default, `git cliff --bumped-version` derives the next tag from the conventional commits since the previous
semver tag:

- A `feat:` commit bumps the minor version.
- A `fix:`, `perf:`, `refactor:`, `docs:`, `test:`, `security:` commit bumps the patch version.
- A commit with a breaking change footer bumps the major version.

Set the `VERSION` pipeline variable to override the bump (e.g. for the first release, or for a major bump that is
not flagged in commit messages).

## GitHub release

The default branch and tags are mirrored to GitHub. The
[`.github/workflows/release.yml`](../.github/workflows/release.yml) workflow runs on every pushed semver tag,
regenerates the release notes with git-cliff from the commit history, and creates the matching GitHub release.
