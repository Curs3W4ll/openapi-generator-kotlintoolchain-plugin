package projectmetadata

import projectmetadata.test.models.PostDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProjectMetadataTest {
  @Test
  fun `groupId is written into generated build script`() {
    val content = buildGradle().readText()
    assertTrue(content.contains("group 'com.example.test'"), "Expected groupId in build.gradle, got:\n$content")
  }

  @Test
  fun `version is written into generated build script`() {
    val content = buildGradle().readText()
    assertTrue(content.contains("version '2.5.1'"), "Expected version in build.gradle, got:\n$content")
  }

  @Test
  fun `id is written as artifactId into generated settings script`() {
    val content = settingsGradle().readText()
    assertTrue(
      content.contains("rootProject.name = 'my-test-client'"),
      "Expected id in settings.gradle, got:\n$content",
    )
  }

  @Test
  fun `generated model compiles and has correct enum values`() {
    assertEquals("active", PostDto.Status.active.value)
  }

  private fun outputDir(): java.io.File {
    var dir = java.io.File(System.getProperty("user.dir"))
    while (!java.io.File(dir, "project.yaml").exists()) {
      dir = dir.parentFile ?: error("Cannot find project root from ${System.getProperty("user.dir")}")
    }
    return java.io.File(dir, "build/tasks/_project-metadata_generateOpenAPI@openapi-generator")
  }

  private fun buildGradle() = outputDir().resolve("build.gradle")
  private fun settingsGradle() = outputDir().resolve("settings.gradle")
}