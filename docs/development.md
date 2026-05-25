# Development Setup

## Requirements

- **[Mise](https://mise.jdx.dev/installing-mise.html#homebrew)**

Mise will then install the multiple tools automatically.  
See the `[tools]` table header in `mise.toml` for more details.

To see the available mise tasks, use the `mise run` command.

## Build system

- The build system is [Kotlin Toolchain](https://kotlin-toolchain.org/latest/) — no Gradle or Maven needed.
- Use the `./kotlin` executable to configure the project. It will automatically download and use a compatible JDK for
  you.
