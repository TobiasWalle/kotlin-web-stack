package com.tobiaswalle.kotlin.web.stack

import com.tobiaswalle.kotlin.web.stack.core.Application

fun main() {
    val app: Application = mainKoin.get()
    app.start()
}
