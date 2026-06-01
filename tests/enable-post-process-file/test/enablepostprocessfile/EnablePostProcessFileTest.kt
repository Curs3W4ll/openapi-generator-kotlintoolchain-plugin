package enablepostprocessfile

import kotlin.test.Test
import kotlin.test.assertNotNull

class EnablePostProcessFileTest {
  @Test
  fun `API class is generated when enablePostProcessFile is enabled`() {
    val apiClass = Class.forName("org.openapitools.client.apis.DefaultApi")
    assertNotNull(apiClass)
  }
}