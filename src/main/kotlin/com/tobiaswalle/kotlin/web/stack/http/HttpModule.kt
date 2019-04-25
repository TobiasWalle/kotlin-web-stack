package com.tobiaswalle.kotlin.web.stack.http

import io.javalin.Javalin
import org.koin.dsl.module

val HttpModule = module {
  single {
    Javalin
      .create()
      .disableStartupBanner()
      .enableDebugLogging()
  }
}
