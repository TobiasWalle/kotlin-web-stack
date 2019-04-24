package com.tobiaswalle.kotlin.web.stack

import com.tobiaswalle.kotlin.web.stack.application.applicationModule
import com.tobiaswalle.kotlin.web.stack.documentation.documentationModule
import com.tobiaswalle.kotlin.web.stack.users.userModule

val modules = arrayListOf(
  userModule,
  applicationModule,
  documentationModule
)
