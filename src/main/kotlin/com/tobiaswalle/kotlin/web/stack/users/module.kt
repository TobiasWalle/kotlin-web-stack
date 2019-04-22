package com.tobiaswalle.kotlin.web.stack.users

import org.koin.dsl.module

val userModule = module {
    single { UserService() }
    single { UserController(get()) }
}
