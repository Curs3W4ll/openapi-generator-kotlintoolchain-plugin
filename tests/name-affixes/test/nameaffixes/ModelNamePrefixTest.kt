package nameaffixes

import nameaffixes.test.models.PfxPostDtoModel
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelNamePrefixTest {
  @Test
  fun `modelNamePrefix is prepended to every generated model class name`() {
    assertEquals("active", PfxPostDtoModel.Status.active.value)
  }
}