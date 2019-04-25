package com.tobiaswalle.kotlin.web.stack.application

import com.fasterxml.jackson.databind.ObjectMapper
import io.javalin.Javalin
import io.javalin.json.JavalinJackson

class Application(
  private val javalin: Javalin,
  private val objectMapper: ObjectMapper,
  private val applicationRouter: ApplicationRouter
) {
  fun start() {
    JavalinJackson.configure(objectMapper)
    with(javalin) {
      routes(applicationRouter)
      start()
    }
  }
}
