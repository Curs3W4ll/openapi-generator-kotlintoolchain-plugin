package skipoperationexample

import kotlin.test.Test
import kotlin.test.assertNotNull

class SkipOperationExampleTest {
  @Test
  fun `API class is generated when skipOperationExample is enabled`() {
    val apiClass = Class.forName("org.openapitools.client.apis.DefaultApi")
    assertNotNull(apiClass)
  }
}