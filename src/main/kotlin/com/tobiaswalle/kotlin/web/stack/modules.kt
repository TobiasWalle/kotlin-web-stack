package com.tobiaswalle.kotlin.web.stack

import com.tobiaswalle.kotlin.web.stack.application.ApplicationModule
import com.tobiaswalle.kotlin.web.stack.application.JacksonModule
import com.tobiaswalle.kotlin.web.stack.documentation.DocumentationModule
import com.tobiaswalle.kotlin.web.stack.http.HttpModule
import com.tobiaswalle.kotlin.web.stack.users.UserModule

val modules = arrayListOf(
  ApplicationModule,
  JacksonModule,
  DocumentationModule,
  HttpModule,
  UserModule
)
