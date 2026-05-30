package pkgname

import pkgname.test.apis.DefaultApi
import pkgname.test.models.PostDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PackageNameTest {
  @Test
  fun `modelPackage overrides the package for generated model classes`() {
    assertEquals("active", PostDto.Status.active.value)
  }

  @Test
  fun `apiPackage overrides the package for generated API classes`() {
    val api = DefaultApi()
    assertNotNull(api)
  }
}