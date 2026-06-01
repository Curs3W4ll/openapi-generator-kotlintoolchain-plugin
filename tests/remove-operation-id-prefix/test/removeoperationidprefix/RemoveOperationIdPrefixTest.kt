package removeoperationidprefix

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RemoveOperationIdPrefixTest {
  @Test
  fun `removeOperationIdPrefix strips tag prefix from generated method name`() {
    val content = postsApiJava().readText()
    assertTrue(
      content.contains("getPost("),
      "Expected 'getPost(' in PostsApi.java (removeOperationIdPrefix should strip 'Posts_' prefix from operationId 'Posts_GetPost'), got:\n$content",
    )
    assertFalse(
      content.contains("postsGetPost("),
      "Expected no 'postsGetPost(' in PostsApi.java after removeOperationIdPrefix stripped the tag prefix, got:\n$content",
    )
  }

  private fun outputDir(): java.io.File {
    var dir = java.io.File(System.getProperty("user.dir"))
    while (!java.io.File(dir, "project.yaml").exists()) {
      dir = dir.parentFile ?: error("Cannot find project root from ${System.getProperty("user.dir")}")
    }
    return java.io.File(dir, "build/tasks/_remove-operation-id-prefix_generateOpenAPI@openapi-generator")
  }

  private fun postsApiJava() = outputDir().resolve("src/main/java/removeoperationidprefix/test/api/PostsApi.java")
}