package remote.input.spec

import org.openapitools.client.apis.PetApi
import org.openapitools.client.models.Pet
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ApiTest {
  @Test
  fun `PetApi can be instantiated`() {
    val api = PetApi()
    assertNotNull(api)
  }

  @Test
  fun `Pet Status enum exposes spec values`() {
    assertEquals("available", Pet.Status.available.value)
    assertEquals("pending", Pet.Status.pending.value)
    assertEquals("sold", Pet.Status.sold.value)
  }
}