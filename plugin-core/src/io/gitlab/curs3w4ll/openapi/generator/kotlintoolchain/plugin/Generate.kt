package io.gitlab.curs3w4ll.openapi.generator.kotlintoolchain.plugin

import org.jetbrains.amper.plugins.Input
import org.jetbrains.amper.plugins.Output
import org.jetbrains.amper.plugins.TaskAction
import org.openapitools.codegen.DefaultGenerator
import org.openapitools.codegen.config.CodegenConfigurator
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.isRegularFile

@TaskAction
fun openApiGenerate(
  @Input inputSpec: Path,
  @Output outputDir: Path,
  generatorName: String,
  verbose: Boolean?,
  logToStderr: Boolean?,
  dryRun: Boolean?,
  configOptions: Map<String, String>?,
  packageName: String?,
  apiPackage: String?,
  modelPackage: String?,
  invokerPackage: String?,
  modelNamePrefix: String?,
  modelNameSuffix: String?,
  apiNameSuffix: String?,
  groupId: String?,
  id: String?,
  version: String?,
) {
  if (!inputSpec.isRegularFile()) {
    error("The input spec $inputSpec does not exist or is corrupted")
  }

  println("Generating files")

  outputDir.createDirectories()

  val cfg =
    CodegenConfigurator().apply {
      setGeneratorName(generatorName)
      setInputSpec(inputSpec.toString())
      setOutputDir(outputDir.toString())
      addGlobalProperty("apiTests", "false")
      addGlobalProperty("modelTests", "false")
      setVerbose(verbose ?: false)
      setLogToStderr(logToStderr ?: false)
      configOptions?.forEach { (key, value) -> addAdditionalProperty(key, value) }
      packageName?.let { setPackageName(it) }
      apiPackage?.let { setApiPackage(it) }
      modelPackage?.let { setModelPackage(it) }
      invokerPackage?.let { setInvokerPackage(it) }
      modelNamePrefix?.let { setModelNamePrefix(it) }
      modelNameSuffix?.let { setModelNameSuffix(it) }
      apiNameSuffix?.let { setApiNameSuffix(it) }
      groupId?.let { setGroupId(it) }
      id?.let { setArtifactId(it) }
      version?.let { setArtifactVersion(it) }
    }
  DefaultGenerator(dryRun ?: false).opts(cfg.toClientOptInput()).generate()
}