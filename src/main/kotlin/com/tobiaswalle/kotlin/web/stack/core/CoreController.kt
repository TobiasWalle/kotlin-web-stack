package com.tobiaswalle.kotlin.web.stack.core

import com.tobiaswalle.kotlin.web.stack.framework.Controller
import com.tobiaswalle.kotlin.web.stack.users.UserController
import io.javalin.apibuilder.ApiBuilder.path

class CoreController(
    private val userController: UserController
) : Controller {
    override fun addEndpoints() {
        path("users", userController)
    }
}
