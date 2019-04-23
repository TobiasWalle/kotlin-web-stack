package com.tobiaswalle.kotlin.web.stack.documentation

import com.fasterxml.jackson.databind.ObjectMapper
import io.javalin.Javalin

class DocumentationOptions(
  val javalin: Javalin,
  val version: String,
  val title: String,
  val objectMapper: ObjectMapper
)
