package com.tobiaswalle.kotlin.web.stack.http

import io.javalin.Context
import java.io.InputStream

fun Context.htmlStream(stream: InputStream) {
  contentType("text/html")
  result(stream)
}
