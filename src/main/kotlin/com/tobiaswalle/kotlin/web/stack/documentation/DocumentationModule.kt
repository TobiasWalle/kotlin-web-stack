package com.tobiaswalle.kotlin.web.stack.documentation

import org.koin.dsl.module

val DocumentationModule = module {
  single { DocumentationController(get(), get()) }
}
