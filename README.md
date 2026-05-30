# OpenAPI Generator — Kotlin Toolchain Plugin

A [Kotlin Toolchain](https://kotlin-toolchain.org/latest/) plugin that
integrates [OpenAPI Generator](https://openapi-generator.tech/) into the build pipeline.

It exposes a `generateOpenAPI` task that reads an OpenAPI specification file, runs the configured generator, and
automatically registers the produced Kotlin sources as a source set — no manual wiring needed.

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![GitLab](https://img.shields.io/badge/source-GitLab-orange.svg)](https://gitlab.com/curs3_w4ll/openapi-generator-kotlintoolchain-plugin)

## Installation

:warning: The Kotlin Toolchain does not yet have a plugin registry. Until then, the recommended way to consume this
plugin is via a **git submodule**.

### 1. Add the submodule

```sh
git submodule add git@gitlab.com:curs3_w4ll/openapi-generator-kotlintoolchain-plugin.git openapi-generator-plugin
```

### 2. Register the plugin in `project.yaml`

```yaml
plugins:
  - ./openapi-generator-plugin/plugin-core
```

## Usage

### Enable the plugin in `module.yaml`

```yaml
plugins:
  openapi-generator:
    enabled: true
    generatorName: kotlin
    inputSpec: "resources/openapi/api.yml"
```

| Setting          | Required | Description                                                                               |
|------------------|----------|-------------------------------------------------------------------------------------------|
| `enabled`        | yes      | Activates the plugin for this module.                                                     |
| `generatorName`  | yes      | The OpenAPI Generator target (e.g. `kotlin`, `kotlin-server`, …).                         |
| `inputSpec`      | yes      | Path to the OpenAPI spec file, relative to the module root.                               |
| `verbose`        | no       | Enable verbose logging from the generator. Default: `false`.                              |
| `logToStderr`    | no       | Write all log messages to stderr instead of stdout. Default: `false`.                     |
| `dryRun`         | no       | Run the generator without writing any files to disk. Default: `false`.                    |
| `configOptions`  | no       | Generator-specific options as key-value pairs (e.g. `dateLibrary`, `enumPropertyNaming`). |
| `packageName`    | no       | Default package for all generated classes when more specific packages are not set.        |
| `apiPackage`     | no       | Package for generated API interface/implementation classes.                               |
| `modelPackage`   | no       | Package for generated model/DTO classes.                                                  |
| `invokerPackage`  | no       | Root/invoker package used by some generators as the top-level namespace.                  |
| `modelNamePrefix` | no       | Prefix prepended to every generated model class name.                                     |
| `modelNameSuffix` | no       | Suffix appended to every generated model class name.                                      |
| `apiNameSuffix`   | no       | Suffix appended to every generated API class/interface name.                              |
| `groupId`         | no       | GroupId written into generated build scripts (e.g. `pom.xml`, `build.gradle`).            |
| `id`              | no       | ArtifactId written into generated build scripts.                                          |
| `version`         | no       | Artifact version written into generated build scripts.                                    |
| `library`         | no       | Sub-template to use with the selected generator (e.g. `jvm-ktor`, `jvm-okhttp4` for `kotlin`). |
| `gitHost`         | no       | Git host (e.g. `gitlab.com`, `github.com`). Included in generated repository URLs and scripts.  |
| `gitUserId`       | no       | Git user or organisation name. Included in generated repository URLs and scripts.               |
| `gitRepoId`       | no       | Git repository name. Included in generated repository URLs and scripts.                         |
| `releaseNote`     | no       | Release note text embedded in generated changelogs and scripts.                                 |

### Run the generator

```sh
./kotlin run generateOpenAPI
```

The generated sources are automatically registered as a source set — no extra wiring needed. A normal build also
triggers generation:

```sh
./kotlin build
```

### Use the generated code

The generated package root follows the OpenAPI Generator convention for the chosen target. For the `kotlin` generator:

```kotlin
import org.openapitools.client.apis.DefaultApi

fun main() {
  val api = DefaultApi()
  val result = api.getSomething(id = 1)
  println(result)
}
```

Add the runtime dependencies required by the generator to `module.yaml`. For the `kotlin` generator:

```yaml
dependencies:
  - com.squareup.okhttp3:okhttp:4.12.0
  - com.squareup.moshi:moshi-kotlin:1.15.2
```

## Contributing

Contributions are welcome!
See [CONTRIBUTING](CONTRIBUTING.md) for guidelines.

## License

Licensed under the [Apache License 2.0](LICENSE).  
Copyright 2026 Corentin Hervaud.
