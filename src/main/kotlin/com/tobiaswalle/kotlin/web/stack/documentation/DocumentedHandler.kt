package com.tobiaswalle.kotlin.web.stack.documentation

import io.javalin.Context
import io.javalin.Handler

typealias HandleRequest = (ctx: Context) -> Unit

class DocumentedHandler(
    private val handleRequest: HandleRequest
) : Handler {
    override fun handle(ctx: Context) = handleRequest(ctx)
}

fun documented(handle: HandleRequest): DocumentedHandler = DocumentedHandler(handle)
