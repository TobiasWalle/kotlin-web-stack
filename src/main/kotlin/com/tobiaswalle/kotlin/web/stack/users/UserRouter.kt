package com.tobiaswalle.kotlin.web.stack.users

import com.tobiaswalle.kotlin.web.stack.documentation.models.documented
import com.tobiaswalle.kotlin.web.stack.http.Router
import com.tobiaswalle.kotlin.web.stack.users.models.User
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.put

class UserRouter(
  private val userService: UserService
) : Router {
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
