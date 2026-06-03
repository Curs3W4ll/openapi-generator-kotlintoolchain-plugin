package gencontrols

import gencontrols.test.model.PostDto
import kotlin.test.Test
import kotlin.test.assertEquals

class GeneratedSourcesTest {
  @Test
  fun `Java main sources are compiled and accessible as a Java source set`() {
    // PostDto import fails to compile if Java main sources are not registered.
    // getValue() returns the spec-defined string — non-tautological against hardcoded "active".
    assertEquals("active", PostDto.StatusEnum.ACTIVE.getValue())
  }
}