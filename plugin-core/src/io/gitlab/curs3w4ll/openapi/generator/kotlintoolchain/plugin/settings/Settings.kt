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
}