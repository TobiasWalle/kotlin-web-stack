package com.tobiaswalle.kotlin.web.stack.application

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.Javalin
import org.koin.dsl.module

val applicationModule = module {
  single { CoreController(get(), get()) }
  single {
    jacksonObjectMapper()
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
  }
  single { Javalin.create() }
  single { Application(get(), get(), get()) }
}
