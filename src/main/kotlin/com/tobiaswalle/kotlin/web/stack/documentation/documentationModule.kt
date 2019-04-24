package com.tobiaswalle.kotlin.web.stack.documentation

import org.koin.dsl.module

val documentationModule = module {
  single { DocumentationController(get(), get()) }
}
