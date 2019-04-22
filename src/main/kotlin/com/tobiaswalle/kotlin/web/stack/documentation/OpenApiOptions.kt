package com.tobiaswalle.kotlin.web.stack.documentation

import io.javalin.Javalin

class DocumentationOptions(
    val javalin: Javalin,
    val version: String,
    val title: String
)
