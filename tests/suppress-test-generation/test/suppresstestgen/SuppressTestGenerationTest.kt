package suppresstestgen

import suppresstestgen.test.models.PostDto
import kotlin.test.Test
import kotlin.test.assertEquals

class SuppressTestGenerationTest {
  @Test
  fun `Kotlin main sources compile when test generation is disabled`() {
    // PostDto import fails to compile if Kotlin main sources are not registered.
    // .value returns the spec-defined string — non-tautological against hardcoded "active".
    assertEquals("active", PostDto.Status.active.value)
  }
}