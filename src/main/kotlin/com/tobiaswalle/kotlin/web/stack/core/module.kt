package com.tobiaswalle.kotlin.web.stack.core

import io.javalin.Javalin
import org.koin.dsl.module

val coreModule = module {
    single { CoreController(get()) }
    single { Javalin.create().routes(get<CoreController>()) }
    single { Application(get()) }
}
