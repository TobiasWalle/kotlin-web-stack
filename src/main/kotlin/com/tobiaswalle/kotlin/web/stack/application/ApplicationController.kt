package com.tobiaswalle.kotlin.web.stack.application

import com.tobiaswalle.kotlin.web.stack.documentation.DocumentationController
import com.tobiaswalle.kotlin.web.stack.http.Controller
import com.tobiaswalle.kotlin.web.stack.users.UserController

class ApplicationController(
  private val userController: UserController,
  private val documentationController: DocumentationController
) : Controller {
  override fun addEndpoints() {
  }
}
