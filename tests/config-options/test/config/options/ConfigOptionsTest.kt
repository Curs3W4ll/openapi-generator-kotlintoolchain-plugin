package config.options

import org.openapitools.client.models.PostDto
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigOptionsTest {
  @Test
  fun `enum values are generated in UPPERCASE as specified by enumPropertyNaming configOption`() {
    assertEquals("active", PostDto.Status.ACTIVE.value)
  }
}