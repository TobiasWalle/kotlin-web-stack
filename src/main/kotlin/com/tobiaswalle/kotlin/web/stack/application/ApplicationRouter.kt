package com.tobiaswalle.kotlin.web.stack.application

import com.tobiaswalle.kotlin.web.stack.documentation.DocumentationRouter
import com.tobiaswalle.kotlin.web.stack.http.Router
import com.tobiaswalle.kotlin.web.stack.users.UserRouter
import io.javalin.apibuilder.ApiBuilder.path

class ApplicationRouter(
  private val userRouter: UserRouter,
  private val documentationRouter: DocumentationRouter
) : Router {
  override fun addEndpoints() {
    path("users", userRouter)
    path("docs", documentationRouter)
  }
}
