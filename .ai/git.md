# Git workflow rules

Before creating any branch or commit, or adding changelog entries, validate against all rules in this file.

## Branch naming rules

Allowed characters: lowercase letters, numbers, hyphens (`-`). No spaces. No uppercase.

Always add the issue number at the start of the branch name. This is required.

Use this pattern unless a different name is specified: `<issue-number>-<description>`

Do not use 40-character hexadecimal strings (these conflict with Git commit hashes).

## Commit message rules

### Conventional Commits (REQUIRED)

All commit subjects MUST follow conventional commit format:

**Format:** `<type>(<scope>): <description>`

**Types:**

- `feat:` - New features
- `fix:` - Bug fixes that affect the user
- `refactor:` - Code refactoring
- `docs:` - Documentation changes
- `style:` - Code style/formatting (no logic changes)
- `perf:` - Performance improvements
- `test:` - Testing changes
- `chore:` - Maintenance tasks, releases, dependency updates, CI/infrastructure changes, internal changes
- `security:` - Security-related changes

**Scopes:**

- For action-specific changes, use the action name: `generateOpenAPI`, `validateOpenAPI`, etc.
- For internal config changes, use the name of the tool you changed configuration of (`mise`, `gitlab`, `github`, ...)

**Description Style:**

- Capitalize the first word of the description
- Use imperative mood ("add feature" not "added feature")
- Keep it concise but descriptive (72 characters maximum)
- No period at the end
- Minimum 3 words
- No emojis

**Examples:**

- `fix(generateOpenAPI): Resolve version mismatch for previously installed tools`
- `feat(validateOpenAPI): Add apiPackage setting`
- `docs(readme): Update usage with new published artifact`
- `chore: Update changelog`
- `chore(mise): Add cache invalidation task`
- `chore(gitlab): Run tests for MR pipeline`

### Body (conditional)

Required when the commit changes 10 or more files.

If a body is included:

- Separate from subject with one blank line
- 72 characters maximum per line
- Explain why the change is being made, not what it does

## Commit trailers

Do not add `Co-Authored-By:` trailers to commit messages.

## Commit granularity

Each commit should represent one coherent concern with a single review context and a clean rollback profile. A reviewer
should be able to evaluate a commit without needing to understand changes in sibling commits.

When a change spans multiple concern types, split proactively before the first commit — not after a reviewer flags it.
Common concern boundaries:

| Concern           | What it contains                                                                                                                                         |
|-------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| Documentation     | `*.md` files (README, guides, changelogs), API docs — standalone doc-only changes, or docs that accompany a code change but are independently reviewable |
| Implementation    | The functional change: plugin actions, models, business logic                                                                                            |
| Tests             | Tests projects for the implementation — travel with the implementation commit unless the suite is large enough to warrant a dedicated commit             |
| Tooling / scripts | Shell scripts, CI pipeline config, build tooling, linting rules, mise tasks — anything that changes how the project is built, linted, or run             |

These are not mechanical rules — use judgment. The test: could this commit be reverted independently without breaking
the others? If not, it should probably be split further.

## Pre-commit process

This project uses pre-commit to ensure committed code is good enough.

Pre-commit should already be installed by mise hooks, but you can make sure so by running `mise pre-commit:install`

If committing fails, check the output for details — it is likely caused by one of the hooks.