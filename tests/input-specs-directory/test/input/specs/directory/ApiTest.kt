package input.specs.directory

import org.openapitools.client.models.PostDto
import org.openapitools.client.models.UserDto
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiTest {
  @Test
  fun `PostDto Status enum exposes spec values from the referenced posts file`() {
    assertEquals("active", PostDto.Status.active.value)
    assertEquals("archived", PostDto.Status.archived.value)
  }

  @Test
  fun `UserDto Role enum exposes spec values from the referenced users file`() {
    assertEquals("admin", UserDto.Role.admin.value)
    assertEquals("member", UserDto.Role.member.value)
  }
}