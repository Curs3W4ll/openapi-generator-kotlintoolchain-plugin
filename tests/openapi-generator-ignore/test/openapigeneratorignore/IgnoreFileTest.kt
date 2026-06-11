package openapigeneratorignore

import org.openapitools.client.models.PostDto
import kotlin.test.Test
import kotlin.test.assertFailsWith

class IgnoreFileTest {
  @Test
  fun `model not in any ignore list is generated and loadable`() {
    // PostDto import fails to compile if the model file was not generated.
    // Reflective lookup proves the class is also present on the runtime classpath.
    Class.forName(PostDto::class.java.name)
  }

  @Test
  fun `model matched by ignoreFileOverride is absent from the classpath`() {
    assertFailsWith<ClassNotFoundException> {
      Class.forName("org.openapitools.client.models.IgnoredFromFileDto")
    }
  }

  @Test
  fun `model matched by openapiGeneratorIgnoreList is absent from the classpath`() {
    assertFailsWith<ClassNotFoundException> {
      Class.forName("org.openapitools.client.models.IgnoredFromListDto")
    }
  }
}