package com.tobiaswalle.kotlin.web.stack.users

import com.tobiaswalle.kotlin.web.stack.http.Router
import com.tobiaswalle.kotlin.web.stack.users.models.User
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.put
import io.javalin.plugin.openapi.dsl.document
import io.javalin.plugin.openapi.dsl.documented

class UserRouter(
  private val userService: UserService
) : Router {
  override fun addEndpoints() {
    put("/", documented(putUserDocs) {
      val user = it.body<User>()
      userService.add(user)
    })

    get("/", documented(getUsersDocs) {
      it.json(userService.getAll())
    })
  }
}

val putUserDocs = document()
  .body<User>()

val getUsersDocs = document()
  .jsonArray<User>("200")
