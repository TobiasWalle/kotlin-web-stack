package com.tobiaswalle.kotlin.web.stack.application

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.koin.dsl.module

val JacksonModule = module {
  single {
    jacksonObjectMapper()
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
  }
  single {
    get<ObjectMapper>()
      .writerWithDefaultPrettyPrinter()
  }
}
