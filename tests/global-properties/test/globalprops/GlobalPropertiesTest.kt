package globalprops

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GlobalPropertiesTest {
  @Test
  fun `globalProperties modelDocs false suppresses model documentation file generation`() {
    val docFile = outputDir().resolve("docs/PostDto.md")
    assertFalse(
      docFile.exists(),
      "Expected docs/PostDto.md to be absent (from globalProperties {modelDocs: false}), but it exists",
    )
  }

  @Test
  fun `globalProperties does not suppress api docs when only modelDocs is set to false`() {
    val docFile = outputDir().resolve("docs/DefaultApi.md")
    assertTrue(
      docFile.exists(),
      "Expected docs/DefaultApi.md to exist (apiDocs not suppressed), but it was absent",
    )
  }

  @Test
  fun `additionalProperties serializableModel makes generated model implement Serializable`() {
    val content = postDtoJava().readText()
    assertTrue(
      content.contains("implements Serializable"),
      "Expected PostDto to implement Serializable (from additionalProperties {serializableModel: true}), got:\n$content",
    )
  }

  private fun outputDir(): java.io.File {
    var dir = java.io.File(System.getProperty("user.dir"))
    while (!java.io.File(dir, "project.yaml").exists()) {
      dir = dir.parentFile ?: error("Cannot find project root from ${System.getProperty("user.dir")}")
    }
    return java.io.File(dir, "build/tasks/_global-properties_generateOpenAPI@openapi-generator")
  }

  private fun postDtoJava() = outputDir().resolve("src/main/java/globalprops/test/model/PostDto.java")
}