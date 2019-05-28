package com.tobiaswalle.kotlin.web.stack.http

import cc.vileda.openapi.dsl.response
import cc.vileda.openapi.dsl.responses
import io.javalin.Javalin
import io.javalin.core.util.RouteOverviewPlugin
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.info.Info
import org.koin.dsl.module

val JavalinModule = module {
  single {
    Javalin.create { config ->
      config.showJavalinBanner = false
      config.enableDevLogging()

      val openApiInfo = Info()
        .version("1.0.0")
        .title("My Application")
      val openApiOptions = OpenApiOptions(openApiInfo)
        .path("/docs/openapi")
        .swagger(SwaggerOptions("/docs"))
        .defaultOperation { operation, _ ->
          operation.responses {
            response("500") { }
          }
        }

      config.registerPlugin(
        OpenApiPlugin(openApiOptions)
      )
      config.registerPlugin(RouteOverviewPlugin("/"))
    }
  }
}
