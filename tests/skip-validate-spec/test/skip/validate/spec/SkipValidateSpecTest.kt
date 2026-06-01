package skip.validate.spec

import kotlin.test.Test
import kotlin.test.assertNotNull

class SkipValidateSpecTest {
  @Test
  fun `API class is generated despite spec failing validation`() {
    val apiClass = Class.forName("org.openapitools.client.apis.DefaultApi")
    assertNotNull(apiClass)
  }
}