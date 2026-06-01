package skip.overwrite

import kotlin.test.Test
import kotlin.test.assertNotNull

class SkipOverwriteTest {
  @Test
  fun `API class is generated when skipOverwrite is enabled`() {
    val apiClass = Class.forName("org.openapitools.client.apis.DefaultApi")
    assertNotNull(apiClass)
  }
}