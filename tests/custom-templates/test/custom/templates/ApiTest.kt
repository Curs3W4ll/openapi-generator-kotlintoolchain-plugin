package custom.templates

import org.openapitools.client.infrastructure.RequestMethod
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiTest {
  @Test
  fun `RequestMethod is generated from the overridden Mustache template in templateDir`() {
    assertEquals("custom-template-marker", RequestMethod.CUSTOM_TEMPLATE_MARKER)
  }
}