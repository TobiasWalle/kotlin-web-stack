package com.tobiaswalle.kotlin.web.stack.application

import io.javalin.Javalin
import org.koin.dsl.module

val applicationModule = module {
    single { CoreController(get()) }
    single { Javalin.create().routes(get<CoreController>()) }
    single { Application(get()) }
}
