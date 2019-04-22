package com.tobiaswalle.kotlin.web.stack.core

import io.javalin.Javalin

class Application(
    private val javalin: Javalin
) {
    fun start() {
        javalin.start()
    }
}
