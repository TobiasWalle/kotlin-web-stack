package com.tobiaswalle.kotlin.web.stack.documentation

import com.fasterxml.jackson.databind.ObjectMapper
import com.tobiaswalle.kotlin.web.stack.documentation.configuration.DocumentationOptions
import com.tobiaswalle.kotlin.web.stack.documentation.generation.OpenApiJavalin
import com.tobiaswalle.kotlin.web.stack.http.Router
import com.tobiaswalle.kotlin.web.stack.http.htmlStream
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get

class DocumentationRouter(
  private val javalin: Javalin,
  private val objectMapper: ObjectMapper
) : Router {
  override fun addEndpoints() {
    get("swagger.json") {
      it.json(
        OpenApiJavalin.createSchema(
          DocumentationOptions(
            javalin = javalin,
            version = "1.0",
            title = "MyApi",
            objectMapper = objectMapper
          )
        )
      )
    }

    get("swagger-ui") {
      it.htmlStream(javaClass.classLoader.getResourceAsStream("documentation/swagger-ui.html")!!)
    }
  }
}
