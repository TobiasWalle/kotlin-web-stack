package com.tobiaswalle.kotlin.web.stack.testing

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

internal val objectMapper: ObjectMapper = jacksonObjectMapper()
  .setSerializationInclusion(JsonInclude.Include.NON_NULL)

internal val objectWriter: ObjectWriter = objectMapper
  .writerWithDefaultPrettyPrinter()
