package apinamesuffix

import apinamesuffix.test.api.DefaultGateway
import apinamesuffix.test.model.PostDto
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiNameSuffixTest {
  @Test
  fun `apiNameSuffix is applied to every generated API class name`() {
    // DefaultGateway import fails to compile if apiNameSuffix is not applied
    assertEquals("active", PostDto.StatusEnum.ACTIVE.getValue())
  }
}