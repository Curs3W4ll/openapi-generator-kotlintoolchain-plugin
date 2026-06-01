package servervariables

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ServerVariablesAndMappingsTest {
  @Test
  fun `reservedWordsMappings renames return property to returnValue in generated model`() {
    val content = postDtoJava().readText()
    assertTrue(
      content.contains("returnValue"),
      "Expected 'returnValue' in PostDto.java (from reservedWordsMappings {return: returnValue}), got:\n$content",
    )
    assertFalse(
      content.contains("_return"),
      "Expected '_return' to be absent after reservedWordsMappings remapped return to returnValue, got:\n$content",
    )
  }

  @Test
  fun `serverVariables substitutes scheme in generated API client base path`() {
    val content = apiClientJava().readText()
    assertTrue(
      content.contains("https://api.example.com"),
      "Expected 'https://api.example.com' in ApiClient.java (from serverVariables {scheme: https}), got:\n$content",
    )
    assertFalse(
      content.contains("http://api.example.com"),
      "Expected 'http://api.example.com' to be absent after serverVariables overrode scheme to https, got:\n$content",
    )
  }

  @Test
  fun `openapiNormalizer SET_TAGS_FOR_ALL_OPERATIONS assigns operations to correct API class`() {
    val apiDir = outputDir().resolve("src/main/java/servervariables/test/api")
    val files = apiDir.listFiles()?.map { it.name } ?: emptyList()
    assertTrue(
      files.contains("BlogApi.java"),
      "Expected 'BlogApi.java' in api dir (from openapiNormalizer {SET_TAGS_FOR_ALL_OPERATIONS: blog}), got: $files",
    )
    assertFalse(
      files.contains("DefaultApi.java"),
      "Expected 'DefaultApi.java' to be absent after openapiNormalizer assigned operations to 'blog' tag, got: $files",
    )
  }

  private fun outputDir(): java.io.File {
    var dir = java.io.File(System.getProperty("user.dir"))
    while (!java.io.File(dir, "project.yaml").exists()) {
      dir = dir.parentFile ?: error("Cannot find project root from ${System.getProperty("user.dir")}")
    }
    return java.io.File(dir, "build/tasks/_server-variables-and-mappings_generateOpenAPI@openapi-generator")
  }

  private fun postDtoJava() = outputDir().resolve("src/main/java/servervariables/test/model/PostDto.java")

  private fun apiClientJava() = outputDir().resolve("src/main/java/servervariables/test/ApiClient.java")
}