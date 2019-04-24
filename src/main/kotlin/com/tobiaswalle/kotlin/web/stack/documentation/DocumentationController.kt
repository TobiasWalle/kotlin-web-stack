package com.tobiaswalle.kotlin.web.stack.documentation

import com.fasterxml.jackson.databind.ObjectMapper
import com.tobiaswalle.kotlin.web.stack.framework.Controller
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get

class DocumentationController(
  private val javalin: Javalin,
  private val objectMapper: ObjectMapper
) : Controller {
  override fun addEndpoints() {
    get("swagger.json") {
      it.json(OpenApiJavalin.createSchema(
        DocumentationOptions(
        javalin = javalin,
        version = "1.0",
        title = "MyApi",
        objectMapper = objectMapper
      )))
    }
  }
}
