package simple.config.client

import custom.config.api.apis.DefaultApi

fun main() {
  val api = DefaultApi()
  val post = api.getPost(id = 1)
  println("Fetched post: $post")
}