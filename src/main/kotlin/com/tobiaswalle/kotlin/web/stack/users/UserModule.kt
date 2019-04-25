package com.tobiaswalle.kotlin.web.stack.users

import org.koin.dsl.module

val UserModule = module {
  single { UserService() }
  single { UserController(get()) }
}
