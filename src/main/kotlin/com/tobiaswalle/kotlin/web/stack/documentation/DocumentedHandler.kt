package com.tobiaswalle.kotlin.web.stack.documentation

import io.javalin.Context
import io.javalin.Handler

typealias HandleRequest = (ctx: Context) -> Unit

fun documented(handle: HandleRequest): DocumentedHandler = DocumentedHandler(handle)

class DocumentedHandler(
  private val handleRequest: HandleRequest
) : Handler {
  override fun handle(ctx: Context) = handleRequest(ctx)

  val responses = mutableMapOf<String, DocumentedResponse>()

  inline fun <reified T> respondWith(name: String): DocumentedHandler {
    return respondWith(name, T::class.java)
  }

  fun respondWith(name: String, returnType: Class<*>): DocumentedHandler {
    responses[name] = DocumentedResponse(name, returnType)
    return this
  }
}

class DocumentedResponse(
  val name: String,
  val returnType: Class<*>
)
