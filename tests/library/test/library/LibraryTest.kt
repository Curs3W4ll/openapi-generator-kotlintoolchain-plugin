package library

import library.test.apis.DefaultApi
import library.test.model.PostDto
import kotlin.test.Test
import kotlin.test.assertEquals

class LibraryTest {
  @Test
  fun `library selects the native sub-template`() {
    // DefaultApi compiles only when library: native is applied — the default
    // (okhttp-gson) would require okhttp/gson dependencies that are absent here,
    // failing compilation if the setting is ignored.
    assertEquals("active", PostDto.StatusEnum.ACTIVE.getValue())
  }
}