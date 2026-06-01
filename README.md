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

| Setting                      | Required | Description                                                                                                    |
|------------------------------|----------|----------------------------------------------------------------------------------------------------------------|
| `enabled`                    | yes      | Activates the plugin for this module.                                                                          |
| `generatorName`              | yes      | The OpenAPI Generator target (e.g. `kotlin`, `kotlin-server`, …).                                              |
| `inputSpec`                  | yes      | Path to the OpenAPI spec file, relative to the module root.                                                    |
| `verbose`                    | no       | Enable verbose logging from the generator. Default: `false`.                                                   |
| `logToStderr`                | no       | Write all log messages to stderr instead of stdout. Default: `false`.                                          |
| `dryRun`                     | no       | Run the generator without writing any files to disk. Default: `false`.                                         |
| `validateSpec`               | no       | Enable or disable spec validation before code generation. Default: `true`.                                     |
| `skipValidateSpec`           | no       | Skip spec validation entirely. Equivalent to `validateSpec: false`. Default: `false`.                          |
| `globalProperties`           | no       | Global property overrides for the generator (e.g. `{apiTests: "false", debugModels: "true"}`).                 |
| `configOptions`              | no       | Generator-specific options as key-value pairs (e.g. `dateLibrary`, `enumPropertyNaming`).                      |
| `additionalProperties`       | no       | Extra properties made available inside Mustache/Handlebars templates (e.g. `{serializableModel: "true"}`).     |
| `packageName`                | no       | Default package for all generated classes when more specific packages are not set.                             |
| `apiPackage`                 | no       | Package for generated API interface/implementation classes.                                                    |
| `modelPackage`               | no       | Package for generated model/DTO classes.                                                                       |
| `invokerPackage`             | no       | Root/invoker package used by some generators as the top-level namespace.                                       |
| `modelNamePrefix`            | no       | Prefix prepended to every generated model class name.                                                          |
| `modelNameSuffix`            | no       | Suffix appended to every generated model class name.                                                           |
| `apiNameSuffix`              | no       | Suffix appended to every generated API class/interface name.                                                   |
| `groupId`                    | no       | GroupId written into generated build scripts (e.g. `pom.xml`, `build.gradle`).                                 |
| `id`                         | no       | ArtifactId written into generated build scripts.                                                               |
| `version`                    | no       | Artifact version written into generated build scripts.                                                         |
| `library`                    | no       | Sub-template to use with the selected generator (e.g. `jvm-ktor`, `jvm-okhttp4` for `kotlin`).                 |
| `gitHost`                    | no       | Git host (e.g. `gitlab.com`, `github.com`). Included in generated repository URLs and scripts.                 |
| `gitUserId`                  | no       | Git user or organisation name. Included in generated repository URLs and scripts.                              |
| `gitRepoId`                  | no       | Git repository name. Included in generated repository URLs and scripts.                                        |
| `releaseNote`                | no       | Release note text embedded in generated changelogs and scripts.                                                |
| `typeMappings`               | no       | Map from OpenAPI type names to language-specific types (e.g. `{DateTime: Instant}`).                           |
| `instantiationTypes`         | no       | Map from OpenAPI container types to their concrete instantiation classes (e.g. `{array: LinkedList}`).         |
| `importMappings`             | no       | Map from class names to fully-qualified import paths (e.g. `{Instant: java.time.Instant}`).                    |
| `languageSpecificPrimitives` | no       | Additional types to treat as language primitives — not wrapped in model classes (e.g. `[Instant, LocalDate]`). |
| `nameMappings`               | no       | Map from schema property names to generated property names (e.g. `{title: heading}`).                          |
| `parameterNameMappings`      | no       | Map from operation parameter names to generated parameter names (e.g. `{id: postId}`).                         |
| `modelNameMappings`          | no       | Map from schema/model names to generated class names (e.g. `{PostDto: BlogPostDto}`).                          |
| `enumNameMappings`           | no       | Map from enum value names to generated enum constant names (e.g. `{active: ENABLED}`).                         |
| `operationIdNameMappings`    | no       | Map from operation IDs to generated method names (e.g. `{getPost: fetchPost}`).                                |

> **Note — default `globalProperties`:** The plugin currently defaults `apiTests` and `modelTests` to `"false"` in
> `globalProperties`. This is a temporary workaround: the Kotlin Toolchain registers the generator's entire output
> directory as a source set, so generated test files (which import JUnit) would fail to compile. Override these
> explicitly in `globalProperties` if you need test files (e.g. to compile them separately). These defaults will be
> removed once the Kotlin Toolchain supports declaring generated test source sets.

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
