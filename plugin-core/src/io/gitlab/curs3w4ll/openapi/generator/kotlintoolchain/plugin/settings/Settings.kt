package io.gitlab.curs3w4ll.openapi.generator.kotlintoolchain.plugin.settings

import org.jetbrains.amper.plugins.Configurable
import java.nio.file.Path

@Configurable
interface Settings {
  /**
   * Path to a local OpenAPI specification file, relative to the module root.
   * Mutually exclusive with [remoteInputSpec] — exactly one of the two must be set.
   */
  val inputSpec: Path?

  /**
   * Path to a directory containing OpenAPI spec files referenced from [inputSpec] (e.g. via `$ref`),
   * relative to the module root. Declaring this directory ensures changes to referenced files
   * invalidate cached generation output. Used alongside [inputSpec].
   */
  val inputSpecsDirectory: Path?

  /**
   * URL to a remote OpenAPI 2.0/3.x specification (e.g. a running service's `/openapi.json`,
   * a raw GitHub URL, or an internal API gateway).
   * Mutually exclusive with [inputSpec] — exactly one of the two must be set.
   */
  val remoteInputSpec: String?

  /**
   * Authorization header(s) used when fetching [remoteInputSpec]
   * (e.g. `"Authorization: Bearer <token>"`). Multiple headers can be comma-separated.
   */
  val auth: String?

  /**
   * Custom HTTP `User-Agent` string used when fetching [remoteInputSpec].
   */
  val httpUserAgent: String?

  /**
   * Path to a JSON configuration file for the generator, relative to the module root.
   */
  val configFile: Path?

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
   * Explicitly enable or disable spec validation before code generation. Default: true.
   * Set to false to allow generation from specs with known validation errors.
   */
  val validateSpec: Boolean?

  /**
   * Skip spec validation entirely before code generation. Default: false.
   * Equivalent to setting validateSpec to false.
   */
  val skipValidateSpec: Boolean?

  /**
   * Clean up the output directory before generation begins. Default: false.
   * When true, all files in the output directory are deleted before generating new code,
   * preventing stale files from accumulating when endpoints or models are removed from the spec.
   */
  val cleanupOutput: Boolean?

  /**
   * Prevent the generator from overwriting files that already exist in the output directory. Default: false.
   * When true, only new files are written; hand-edited generated files are preserved across runs.
   */
  val skipOverwrite: Boolean?

  /**
   * Global property overrides passed to the generator, replacing built-in defaults
   * (e.g. `{"apiTests": "false", "debugModels": "true"}`).
   */
  val globalProperties: Map<String, String>?

  /**
   * Generator-specific configuration options passed as key-value pairs.
   */
  val configOptions: Map<String, String>?

  /**
   * Extra properties made available inside Mustache/Handlebars templates
   * (e.g. `{"serializableModel": "true", "hideGenerationTimestamp": "true"}`).
   */
  val additionalProperties: Map<String, String>?

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

  /**
   * Maps schema property names to generated property names
   * (e.g. `{"title": "heading"}`).
   */
  val nameMappings: Map<String, String>?

  /**
   * Maps operation parameter names to generated parameter names
   * (e.g. `{"id": "postId"}`).
   */
  val parameterNameMappings: Map<String, String>?

  /**
   * Maps schema/model names to generated class names
   * (e.g. `{"PostDto": "BlogPostDto"}`).
   */
  val modelNameMappings: Map<String, String>?

  /**
   * Maps enum value names to generated enum constant names
   * (e.g. `{"active": "ENABLED"}`).
   */
  val enumNameMappings: Map<String, String>?

  /**
   * Maps operation IDs to generated method names
   * (e.g. `{"getPost": "fetchPost"}`).
   */
  val operationIdNameMappings: Map<String, String>?

  /**
   * Maps spec schema names to replacement class names or fully-qualified types
   * (e.g. `{"PostDto": "com.example.PostDto"}`).
   */
  val schemaMappings: Map<String, String>?

  /**
   * Renames auto-generated names for inline schemas
   * (e.g. `{"ListPosts200Response": "PostCollection"}`).
   */
  val inlineSchemaNameMappings: Map<String, String>?

  /**
   * Options controlling how inline schemas are handled
   * (e.g. `{"RESOLVE_INLINE_ENUMS": "true"}`).
   */
  val inlineSchemaOptions: Map<String, String>?

  /**
   * Overrides how reserved words in the target language are escaped
   * (e.g. `{"class": "clazz"}`). By default the generator prefixes with an underscore.
   */
  val reservedWordsMappings: Map<String, String>?

  /**
   * Variable substitutions for server URL templates defined in the spec
   * (e.g. `{"scheme": "https", "env": "prod"}`).
   */
  val serverVariables: Map<String, String>?

  /**
   * Rules passed to the OpenAPI normalizer that fix or transform the parsed spec before
   * code generation (e.g. `{"SET_TAGS_FOR_ALL_OPERATIONS": "blog"}`).
   */
  val openapiNormalizer: Map<String, String>?

  /**
   * Strip the prefix (everything before the first `_`) from operation IDs. Default: false.
   * When true, an operation ID like `Pets_GetPets` becomes `getPets`.
   */
  val removeOperationIdPrefix: Boolean?

  /**
   * Do not include operation examples from the spec in generated code. Default: false.
   */
  val skipOperationExample: Boolean?

  /**
   * Enable the external post-processing hook executed after each file is generated. Default: false.
   * The command to run is read from the `OPENAPI_GENERATOR_IGNORE_FILE_OVERRIDE` environment variable.
   */
  val enablePostProcessFile: Boolean?

  /**
   * Path to a directory containing custom Mustache/Handlebars templates that override the
   * generator's built-in templates, relative to the module root. Template files placed under
   * this directory shadow the embedded templates with the same relative path.
   */
  val templateDir: Path?

  /**
   * Template engine to use when rendering templates: `"mustache"` (default) or `"handlebars"`.
   */
  val templateEngine: String?

  /**
   * Path to a custom `.openapi-generator-ignore` file used instead of the one in the output
   * directory, relative to the module root. Patterns inside follow the gitignore syntax and
   * exclude matching files from generation (e.g. test stubs, documentation, build scripts).
   */
  val ignoreFileOverride: Path?

  /**
   * Inline list of gitignore-style patterns added to the generator ignore list without requiring
   * a separate ignore file. Combined with any patterns loaded from [ignoreFileOverride] or the
   * default ignore file.
   */
  val openapiGeneratorIgnoreList: List<String>?
}