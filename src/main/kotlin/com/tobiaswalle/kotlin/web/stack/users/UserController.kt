package com.tobiaswalle.kotlin.web.stack.users

import com.tobiaswalle.kotlin.web.stack.framework.Controller
import com.tobiaswalle.kotlin.web.stack.users.models.User
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.put

class UserController(
  private val userService: UserService
) : Controller {
  override fun addEndpoints() {
    put("/") {
      val user = it.body<User>()
      userService.add(user)
    }

    get("/") {
      it.json(userService.getAll())
    }
  }
}
