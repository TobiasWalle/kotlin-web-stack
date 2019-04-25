package com.tobiaswalle.kotlin.web.stack.documentation.models

import io.javalin.Context
import io.javalin.Handler
import kotlin.reflect.KClass

class DocumentedHandler(
  private val handleRequest: HandleRequest
) : Handler {
  override fun handle(ctx: Context) = handleRequest(ctx)

  val responses = mutableMapOf<String, DocumentedResponse>()

  fun respondWith(name: String, returnType: KClass<*>): DocumentedHandler {
    return respondWith(name, returnType.java)
  }

  fun respondWith(name: String, returnType: Class<*>): DocumentedHandler {
    responses[name] = DocumentedResponse(name, returnType)
    return this
  }
}

fun documented(handle: HandleRequest): DocumentedHandler = DocumentedHandler(handle)

typealias HandleRequest = (ctx: Context) -> Unit
