package io.gitlab.curs3w4ll.openapi.generator.kotlintoolchain.plugin.settings

import org.jetbrains.amper.plugins.Configurable

@Configurable
interface Settings {
  /**
   * The Open API specification location.
   */
  val inputSpec: String

  /**
   * The name of the generator which will handle codegen.
   */
  val generatorName: String

  /**
   * Enable verbose logging from the generator. Default: false.
   */
  val verbose: Boolean?

  /**
   * Write all generator log messages to stderr instead of stdout. Default: false.
   */
  val logToStderr: Boolean?

  /**
   * Run the generator without writing any files to disk. Default: false.
   */
  val dryRun: Boolean?

  /**
   * Generator-specific configuration options passed as key-value pairs.
   */
  val configOptions: Map<String, String>?

  /**
   * Default package for all generated classes when more specific packages are not set.
   */
  val packageName: String?

  /**
   * Package for generated API interface/implementation classes.
   */
  val apiPackage: String?

  /**
   * Package for generated model/DTO classes.
   */
  val modelPackage: String?

  /**
   * Root/invoker package used by some generators as the top-level namespace.
   */
  val invokerPackage: String?

  /**
   * Prefix prepended to every generated model class name.
   */
  val modelNamePrefix: String?

  /**
   * Suffix appended to every generated model class name.
   */
  val modelNameSuffix: String?

  /**
   * Suffix appended to every generated API class/interface name.
   */
  val apiNameSuffix: String?

  /**
   * GroupId written into generated build scripts (e.g. pom.xml, build.gradle).
   */
  val groupId: String?

  /**
   * ArtifactId written into generated build scripts.
   */
  val id: String?

  /**
   * Artifact version written into generated build scripts.
   */
  val version: String?

  /**
   * Sub-template (library) to use with the selected generator.
   * Available values depend on the chosen generatorName (e.g. jvm-ktor, jvm-okhttp4 for kotlin).
   */
  val library: String?

  /**
   * Git host (e.g. `gitlab.com`, `github.com`). Included in generated repository URLs and scripts.
   */
  val gitHost: String?

  /**
   * Git user or organization name. Included in generated repository URLs and scripts.
   */
  val gitUserId: String?

  /**
   * Git repository name. Included in generated repository URLs and scripts.
   */
  val gitRepoId: String?

  /**
   * Release note text embedded in generated changelogs and scripts.
   */
  val releaseNote: String?

  /**
   * Maps OpenAPI type names to language-specific types (e.g. `{"DateTime": "Instant"}`).
   */
  val typeMappings: Map<String, String>?

  /**
   * Maps OpenAPI container types to their concrete instantiation classes
   * (e.g. `{"array": "LinkedList"}`).
   */
  val instantiationTypes: Map<String, String>?

  /**
   * Maps class names to fully-qualified import paths
   * (e.g. `{"Instant": "java.time.Instant"}`).
   */
  val importMappings: Map<String, String>?

  /**
   * Additional type names to treat as language primitives, preventing the generator
   * from wrapping them in model classes (e.g. `["Instant", "LocalDate"]`).
   */
  val languageSpecificPrimitives: List<String>?
}