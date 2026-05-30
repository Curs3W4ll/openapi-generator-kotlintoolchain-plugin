package nameaffixes

import nameaffixes.test.models.PfxPostDtoModel
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelNameSuffixTest {
  @Test
  fun `modelNameSuffix is appended to every generated model class name`() {
    assertEquals("active", PfxPostDtoModel.Status.active.value)
  }
}