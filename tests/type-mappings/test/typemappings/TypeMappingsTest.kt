package typemappings

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TypeMappingsTest {
  @Test
  fun `typeMappings replaces DateTime with Instant in generated model`() {
    val content = postDtoJava().readText()
    assertTrue(
      content.contains("Instant"),
      "Expected 'Instant' type in PostDto.java (from typeMappings {DateTime: Instant}), got:\n$content",
    )
  }

  @Test
  fun `importMappings adds java-time-Instant import to generated model`() {
    val content = postDtoJava().readText()
    assertTrue(
      content.contains("import java.time.Instant"),
      "Expected 'import java.time.Instant' in PostDto.java (from importMappings {Instant: java.time.Instant}), got:\n$content",
    )
  }

  @Test
  fun `instantiationTypes changes array instantiation to the configured type`() {
    val content = postDtoJava().readText()
    assertTrue(
      content.contains("java.util.LinkedList"),
      "Expected 'java.util.LinkedList' instantiation in PostDto.java (from instantiationTypes {array: java.util.LinkedList}), got:\n$content",
    )
  }

  @Test
  fun `languageSpecificPrimitives suppresses model class generation for the listed type`() {
    val files = outputDir().resolve("src/main/java/typemappings/test/model").listFiles() ?: emptyArray()
    val modelNames = files.map { it.name }
    assertFalse(
      modelNames.contains("PostLabel.java"),
      "PostLabel.java should not be generated because PostLabel is declared as a language-specific primitive",
    )
  }

  private fun outputDir(): java.io.File {
    var dir = java.io.File(System.getProperty("user.dir"))
    while (!java.io.File(dir, "project.yaml").exists()) {
      dir = dir.parentFile ?: error("Cannot find project root from ${System.getProperty("user.dir")}")
    }
    return java.io.File(dir, "build/tasks/_type-mappings_generateOpenAPI@openapi-generator")
  }

  private fun postDtoJava() = outputDir().resolve("src/main/java/typemappings/test/model/PostDto.java")
}