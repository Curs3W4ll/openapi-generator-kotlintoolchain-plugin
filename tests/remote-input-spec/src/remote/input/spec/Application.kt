package remote.input.spec

import org.openapitools.client.apis.PetApi

fun main() {
  val api = PetApi()
  val pet = api.getPetById(petId = 1)
  println("Fetched pet: $pet")
}