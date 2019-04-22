package com.tobiaswalle.kotlin.web.stack

import com.tobiaswalle.kotlin.web.stack.core.coreModule
import com.tobiaswalle.kotlin.web.stack.users.userModule
import org.koin.dsl.koinApplication

val mainKoin = koinApplication {
    modules(userModule)
    modules(coreModule)
}.koin
