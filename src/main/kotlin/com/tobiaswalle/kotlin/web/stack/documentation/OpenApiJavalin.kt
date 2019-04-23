package com.tobiaswalle.kotlin.web.stack.documentation

import io.javalin.HandlerMetaInfo
import io.javalin.core.HandlerType
import io.swagger.v3.oas.models.*
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses

object OpenApiJavalin {
  fun createSchema(options: DocumentationOptions): OpenAPI {
    val openApiComponents = OpenApiComponents()
    return OpenAPI().apply {
      info = createOpenApiInfo(options)
      paths = createPaths(options, openApiComponents)
      components = Components().schemas(openApiComponents.schemas)
    }
  }

  private fun createOpenApiInfo(options: DocumentationOptions): Info {
    return Info().apply {
      title = options.title
      version = options.version
    }
  }

  private fun createPaths(options: DocumentationOptions, components: OpenApiComponents): Paths {
    val javalin = options.javalin
    return Paths().apply {
      javalin.handlerMetaInfo
        .groupBy { it.path }
        .forEach { (path, metaInfos) ->
          addPathItem(path, createPathItem(options, components, metaInfos))
        }
    }
  }

  private fun createPathItem(
    options: DocumentationOptions,
    components: OpenApiComponents,
    metaInfos: List<HandlerMetaInfo>
  ): PathItem {
    return PathItem().apply {
      metaInfos.forEach { metaInfo ->
        val operation = createOperation(options, components, metaInfo)
        when (metaInfo.httpMethod) {
          HandlerType.GET -> this.get = operation
          HandlerType.PUT -> this.put = operation
          HandlerType.POST -> this.post = operation
          HandlerType.DELETE -> this.delete = operation
          HandlerType.OPTIONS -> this.options = operation
          HandlerType.HEAD -> this.head = operation
          HandlerType.PATCH -> this.patch = operation
          HandlerType.TRACE -> this.trace = operation
          else -> Unit
        }
      }
    }
  }

  private fun createOperation(
    options: DocumentationOptions,
    components: OpenApiComponents,
    metaInfo: HandlerMetaInfo
  ): Operation {
    val documentedHandler: DocumentedHandler? = try {
      metaInfo.handler as DocumentedHandler
    } catch (e: ClassCastException) {
      null
    }
    val objectMapper = options.objectMapper
    return Operation()
      .responses(
        ApiResponses()
          .addApiResponse(
            "200", ApiResponse().description("")
          )
          .apply {
            documentedHandler?.responses
              ?.forEach { (name, response) ->
                val (ref, newDefinitions) = response.returnType.asOpenApiSchema(objectMapper)
                components.addSchemas(newDefinitions)
                addApiResponse(
                  name, ApiResponse()
                    .description("")
                    .content(
                      Content()
                        .addMediaType(
                          "application/json", MediaType()
                            .schema(Schema<Any>().`$ref`(ref))
                        )
                    )
                )
              }
          }
      )
  }
}

class OpenApiComponents {
  val schemas get() = _schemas.toMap()

  private val _schemas: MutableMap<String, Schema<*>> = mutableMapOf()

  fun addSchemas(map: Map<String, Schema<*>>) {
    _schemas.putAll(map)
  }
}
