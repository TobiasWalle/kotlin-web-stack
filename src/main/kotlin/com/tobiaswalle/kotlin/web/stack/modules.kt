package com.tobiaswalle.kotlin.web.stack

import com.tobiaswalle.kotlin.web.stack.application.ApplicationModule
import com.tobiaswalle.kotlin.web.stack.application.JacksonModule
import com.tobiaswalle.kotlin.web.stack.http.JavalinModule
import com.tobiaswalle.kotlin.web.stack.users.UserModule

val modules = arrayListOf(
  ApplicationModule,
  JacksonModule,
  JavalinModule,
  UserModule
)
