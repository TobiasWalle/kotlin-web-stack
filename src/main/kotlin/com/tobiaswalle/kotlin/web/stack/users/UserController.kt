package com.tobiaswalle.kotlin.web.stack.users

import com.tobiaswalle.kotlin.web.stack.documentation.documented
import com.tobiaswalle.kotlin.web.stack.framework.Controller
import com.tobiaswalle.kotlin.web.stack.users.models.User
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.put

class UserController(
  private val userService: UserService
) : Controller {
  override fun addEndpoints() {
    put("/", documented {
      val user = it.body<User>()
      userService.add(user)
    })

    get("/", documented {
      it.json(userService.getAll())
    }.respondWith("200", Array<User>::class))
  }
}
