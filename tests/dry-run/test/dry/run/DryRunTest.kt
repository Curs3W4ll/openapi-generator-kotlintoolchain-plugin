package dry.run

import kotlin.test.Test
import kotlin.test.assertFailsWith

class DryRunTest {
  @Test
  fun `no API classes are generated in dry-run mode`() {
    assertFailsWith<ClassNotFoundException> {
      Class.forName("org.openapitools.client.apis.DefaultApi")
    }
  }
}