package pkgbase

import pkgbase.test.apis.DefaultApi
import pkgbase.test.infrastructure.Serializer
import pkgbase.test.models.PostDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PackageNameBaseTest {
  @Test
  fun `packageName sets the root package for generated model classes`() {
    assertEquals("active", PostDto.Status.active.value)
  }

  @Test
  fun `packageName sets the root package for generated API classes`() {
    val api = DefaultApi()
    assertNotNull(api)
  }

  @Test
  fun `packageName sets the root package for generated infrastructure classes`() {
    assertNotNull(Serializer.moshi)
  }
}