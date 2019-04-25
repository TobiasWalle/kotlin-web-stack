package com.tobiaswalle.kotlin.web.stack

import com.tobiaswalle.kotlin.web.stack.application.Application
import com.tobiaswalle.kotlin.web.stack.logging.KoinLogger
import org.koin.dsl.koinApplication

fun main() {
  val koin = koinApplication {
    logger(KoinLogger())
    modules(modules)
  }.koin
  val app = koin.get<Application>()
  app.start()
}
