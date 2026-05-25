# Repository Agent Guide

This file provides guidance to AI coding agents when working with code in this repository.

## Context Loading

Load the following instruction files based on your current task:

- When working with git, commits, or branches: Read [.ai/git.md](.ai/git.md)
- When working with merge requests: Read [.ai/merge-requests.md](.ai/merge-requests.md)
- When working with issues: Read [.ai/issues.md](.ai/issues.md)
- When working with CI/CD pipelines or .gitlab-ci.yml: Read [.ai/ci-cd.md](.ai/ci-cd.md)
- When working with mise, tools, or task definitions: Read [.ai/mise.md](.ai/mise.md)

### Local Overrides

Read [AGENTS.local.md](AGENTS.local.md)

## Code Architecture

### High-Level Structure

openapi-generator-kotlintoolchain-plugin is a Kotlin Toolchain plugin that ports the OpenAPI Generator features into a
bundled Kotlin Toolchain plugin.  
The codebase follows a simple architecture:

**Plugin definition:**

- `plugin-core/` - Plugin definition module
- `plugin-core/plugin.yaml` - Plugin specifications

**Tests projects:**

- `tests/*` - A bunch of tests projects that help ensuring the plugin works correctly in various setups

**Configuration System:**

- `mise.toml` / `mise.dev.toml` / `.miserc.toml` - See [.ai/mise.md](.ai/mise.md) for the full mise configuration
  guide (profiles, tool placement, task organisation).
- `project.yaml` - Root Kotlin Toolchain configuration. This repository uses the Kotlin Toolchain as a multi-module
  orchestrator tool

### Test Structure

- Test projects in `tests/` directory organized by type (e.g. `tests/simple-ktor-api/`, `tests/simple-ktor-client/`)
- Each `tests/*` project carries its own `test/` source set (Amper convention) with at least one `kotlin.test` class
  that exercises the generated API — providing both compile-time and runtime validation of the plugin output
- Main application code lives in `src/` under a non-`test`-prefixed package (e.g. `simple.ktor.client`)
- Run `./kotlin check` to execute all test source sets across every module

## Build & Test Commands

This project uses the [Kotlin Toolchain](https://kotlin-toolchain.org/latest/) CLI (`./kotlin`), installed automatically
by mise.

Here are common commands:

| Task | Command |
|------|---------|
| Build | `./kotlin build` |
| Test | `./kotlin test` |
