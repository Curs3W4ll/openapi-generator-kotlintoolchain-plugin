package io.gitlab.curs3w4ll.openapi.generator.kotlintoolchain.plugin

import org.jetbrains.amper.plugins.Input
import org.jetbrains.amper.plugins.Output
import org.jetbrains.amper.plugins.TaskAction
import org.openapitools.codegen.DefaultGenerator
import org.openapitools.codegen.config.CodegenConfigurator
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile

@TaskAction
fun openApiGenerate(
  @Input inputSpec: Path?,
  @Input inputSpecsDirectory: Path?,
  remoteInputSpec: String?,
  auth: String?,
  httpUserAgent: String?,
  @Input configFile: Path?,
  // TODO: #46 Use only one @Output Path once the Kotlin Toolchain supports it
  @Output kotlinMainSourcesDir: Path,
  @Output kotlinTestSourcesDir: Path,
  @Output javaMainSourcesDir: Path,
  @Output javaTestSourcesDir: Path,
  generatorName: String,
  verbose: Boolean?,
  logToStderr: Boolean?,
  dryRun: Boolean?,
  validateSpec: Boolean?,
  skipValidateSpec: Boolean?,
  cleanupOutput: Boolean?,
  skipOverwrite: Boolean?,
  globalProperties: Map<String, String>?,
  configOptions: Map<String, String>?,
  additionalProperties: Map<String, String>?,
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
  library: String?,
  gitHost: String?,
  gitUserId: String?,
  gitRepoId: String?,
  releaseNote: String?,
  typeMappings: Map<String, String>?,
  instantiationTypes: Map<String, String>?,
  importMappings: Map<String, String>?,
  languageSpecificPrimitives: List<String>?,
  nameMappings: Map<String, String>?,
  parameterNameMappings: Map<String, String>?,
  modelNameMappings: Map<String, String>?,
  enumNameMappings: Map<String, String>?,
  operationIdNameMappings: Map<String, String>?,
  schemaMappings: Map<String, String>?,
  inlineSchemaNameMappings: Map<String, String>?,
  inlineSchemaOptions: Map<String, String>?,
  reservedWordsMappings: Map<String, String>?,
  serverVariables: Map<String, String>?,
  openapiNormalizer: Map<String, String>?,
  removeOperationIdPrefix: Boolean?,
  skipOperationExample: Boolean?,
  enablePostProcessFile: Boolean?,
) {
  if (inputSpec != null && remoteInputSpec != null) {
    error("Only one of inputSpec or remoteInputSpec may be set, but both were provided")
  }
  if (inputSpec == null && remoteInputSpec == null) {
    error("Either inputSpec or remoteInputSpec must be set")
  }
  if (inputSpec != null && !inputSpec.isRegularFile()) {
    error("The input spec $inputSpec does not exist or is corrupted")
  }
  if (inputSpecsDirectory != null && !inputSpecsDirectory.isDirectory()) {
    error("The input specs directory $inputSpecsDirectory does not exist or is not a directory")
  }

  val specSource = remoteInputSpec ?: inputSpec!!.toString()

  println("Generating files")

  // The four @Output dirs all sit under src/{main,test}/{kotlin,java}/ inside a common root.
  // Derive that root by walking up three levels from kotlinMainSourcesDir.
  // TODO: #46 Use the only @Output Path here to remove `.parent.parent.parent`
  val outputDir = kotlinMainSourcesDir.parent.parent.parent

  if (cleanupOutput == true) {
    outputDir.toFile().deleteRecursively()
  }
  outputDir.createDirectories()

  val baseConfig = if (configFile != null && configFile.isRegularFile()) {
    CodegenConfigurator.fromFile(configFile.toString())
  } else {
    CodegenConfigurator()
  }

  val config =
    baseConfig.apply {
      setGeneratorName(generatorName)
      setInputSpec(specSource)
      auth?.let { setAuth(it) }
      httpUserAgent?.let { setHttpUserAgent(it) }
      setOutputDir(outputDir.toString())
      setVerbose(verbose ?: false)
      setLogToStderr(logToStderr ?: false)
      setValidateSpec((validateSpec ?: true) && (skipValidateSpec != true))
      skipOverwrite?.let { setSkipOverwrite(it) }

      globalProperties?.forEach { (key, value) -> addGlobalProperty(key, value) }
      configOptions?.forEach { (key, value) -> addAdditionalProperty(key, value) }
      additionalProperties?.forEach { (key, value) -> addAdditionalProperty(key, value) }
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
      library?.let { setLibrary(it) }
      gitHost?.let { setGitHost(it) }
      gitUserId?.let { setGitUserId(it) }
      gitRepoId?.let { setGitRepoId(it) }
      releaseNote?.let { setReleaseNote(it) }
      typeMappings?.let { setTypeMappings(it) }
      instantiationTypes?.let { setInstantiationTypes(it) }
      importMappings?.let { setImportMappings(it) }
      languageSpecificPrimitives?.let { setLanguageSpecificPrimitives(it.toSet()) }
      nameMappings?.let { setNameMappings(it) }
      parameterNameMappings?.let { setParameterNameMappings(it) }
      modelNameMappings?.let { setModelNameMappings(it) }
      enumNameMappings?.let { setEnumNameMappings(it) }
      operationIdNameMappings?.let { setOperationIdNameMappings(it) }
      schemaMappings?.let { setSchemaMappings(it) }
      inlineSchemaNameMappings?.let { setInlineSchemaNameMappings(it) }
      inlineSchemaOptions?.let { setInlineSchemaOptions(it) }
      reservedWordsMappings?.let { setReservedWordsMappings(it) }
      serverVariables?.let { setServerVariables(it) }
      openapiNormalizer?.let { setOpenapiNormalizer(it) }
      removeOperationIdPrefix?.let { setRemoveOperationIdPrefix(it) }
      skipOperationExample?.let { setSkipOperationExample(it) }
      enablePostProcessFile?.let { setEnablePostProcessFile(it) }
    }
  DefaultGenerator(dryRun ?: false).opts(config.toClientOptInput()).generate()
}