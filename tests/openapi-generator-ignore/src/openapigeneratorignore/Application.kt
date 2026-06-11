package openapigeneratorignore

import org.openapitools.client.models.PostDto

fun main() {
  val post = PostDto(id = 1L)
  println("PostDto from openapi-generator-ignore test ready: $post")
}