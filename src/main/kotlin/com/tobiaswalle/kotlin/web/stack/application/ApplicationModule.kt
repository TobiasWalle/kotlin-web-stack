package com.tobiaswalle.kotlin.web.stack.application

import org.koin.dsl.module

val ApplicationModule = module {
  single { ApplicationController(get(), get()) }
  single { Application(get(), get(), get()) }
}
