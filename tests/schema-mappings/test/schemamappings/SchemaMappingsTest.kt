package schemamappings

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SchemaMappingsTest {
  @Test
  fun `schemaMappings suppresses model class generation for mapped schema`() {
    val modelDir = outputDir().resolve("src/main/java/schemamappings/test/model")
    val files = modelDir.listFiles()?.map { it.name } ?: emptyList()
    assertFalse(
      files.contains("PostDto.java"),
      "PostDto.java should not be generated when mapped via schemaMappings to java.lang.String, got: $files",
    )
  }

  @Test
  fun `inlineSchemaNameMappings renames listPosts_200_response to PostCollection`() {
    val modelDir = outputDir().resolve("src/main/java/schemamappings/test/model")
    val files = modelDir.listFiles()?.map { it.name } ?: emptyList()
    assertTrue(
      files.contains("PostCollection.java"),
      "Expected 'PostCollection.java' from inlineSchemaNameMappings {listPosts_200_response: PostCollection}, got: $files",
    )
    assertFalse(
      files.contains("ListPosts200Response.java"),
      "ListPosts200Response.java should be absent after inlineSchemaNameMappings remapped it to PostCollection, got: $files",
    )
  }

  @Test
  fun `inlineSchemaOptions RESOLVE_INLINE_ENUMS does not break generation`() {
    val outputDir = outputDir()
    assertTrue(
      outputDir.exists(),
      "Output directory should exist after generation with inlineSchemaOptions {RESOLVE_INLINE_ENUMS: true}",
    )
  }

  private fun outputDir(): java.io.File {
    var dir = java.io.File(System.getProperty("user.dir"))
    while (!java.io.File(dir, "project.yaml").exists()) {
      dir = dir.parentFile ?: error("Cannot find project root from ${System.getProperty("user.dir")}")
    }
    return java.io.File(dir, "build/tasks/_schema-mappings_generateOpenAPI@openapi-generator")
  }
}