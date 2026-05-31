package namemappings

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NameMappingsTest {
  @Test
  fun `modelNameMappings renames PostDto to BlogPostDto`() {
    val modelDir = outputDir().resolve("src/main/java/namemappings/test/model")
    val files = modelDir.listFiles()?.map { it.name } ?: emptyList()
    assertTrue(
      files.contains("BlogPostDto.java"),
      "Expected 'BlogPostDto.java' in model dir (from modelNameMappings {PostDto: BlogPostDto}), got: $files",
    )
  }

  @Test
  fun `nameMappings renames title property to heading in generated model`() {
    val content = blogPostDtoJava().readText()
    assertTrue(
      content.contains("getHeading()"),
      "Expected 'getHeading()' accessor in BlogPostDto.java (from nameMappings {title: heading}), got:\n$content",
    )
    assertFalse(
      content.contains("getTitle()"),
      "Expected 'getTitle()' to be absent after nameMappings remapped title to heading, got:\n$content",
    )
  }

  @Test
  fun `operationIdNameMappings renames getPost operation to fetchPost in generated API`() {
    val content = defaultApiJava().readText()
    assertTrue(
      content.contains("fetchPost"),
      "Expected 'fetchPost' method in DefaultApi.java (from operationIdNameMappings {getPost: fetchPost}), got:\n$content",
    )
    assertFalse(
      content.contains("getPost"),
      "Expected 'getPost' to be absent after operationIdNameMappings remapped it to fetchPost, got:\n$content",
    )
  }

  @Test
  fun `parameterNameMappings renames id parameter to postId in generated API`() {
    val content = defaultApiJava().readText()
    assertTrue(
      content.contains("postId"),
      "Expected 'postId' parameter in DefaultApi.java (from parameterNameMappings {id: postId}), got:\n$content",
    )
  }

  @Test
  fun `enumNameMappings renames active enum value to ENABLED in generated model`() {
    val content = blogPostDtoJava().readText()
    assertTrue(
      content.contains("ENABLED("),
      "Expected 'ENABLED(' enum constant in BlogPostDto.java (from enumNameMappings {active: ENABLED}), got:\n$content",
    )
    assertFalse(
      // Use line-based check to avoid "INACTIVE" matching "ACTIVE" as a substring
      content.lines().any { it.trim().startsWith("ACTIVE(") },
      "Expected no 'ACTIVE(' enum constant declaration after enumNameMappings remapped active to ENABLED, got:\n$content",
    )
  }

  private fun outputDir(): java.io.File {
    var dir = java.io.File(System.getProperty("user.dir"))
    while (!java.io.File(dir, "project.yaml").exists()) {
      dir = dir.parentFile ?: error("Cannot find project root from ${System.getProperty("user.dir")}")
    }
    return java.io.File(dir, "build/tasks/_name-mappings_generateOpenAPI@openapi-generator")
  }

  private fun blogPostDtoJava() = outputDir().resolve("src/main/java/namemappings/test/model/BlogPostDto.java")

  private fun defaultApiJava() = outputDir().resolve("src/main/java/namemappings/test/api/DefaultApi.java")
}