package com.tobiaswalle.kotlin.web.stack

import com.tobiaswalle.kotlin.web.stack.application.Application
import org.koin.dsl.koinApplication

fun main() {
  val koin = koinApplication { modules(modules) }.koin
  val app = koin.get<Application>()
  app.start()
}
