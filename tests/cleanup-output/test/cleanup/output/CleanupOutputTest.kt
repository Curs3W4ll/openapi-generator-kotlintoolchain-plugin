package cleanup.output

import kotlin.test.Test
import kotlin.test.assertNotNull

class CleanupOutputTest {
  @Test
  fun `API class is generated when cleanupOutput is enabled`() {
    val apiClass = Class.forName("org.openapitools.client.apis.DefaultApi")
    assertNotNull(apiClass)
  }
}