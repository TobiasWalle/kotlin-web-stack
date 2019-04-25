package com.tobiaswalle.kotlin.web.stack.documentation.generation

import com.tobiaswalle.kotlin.web.stack.documentation.configuration.DocumentationOptions
import com.tobiaswalle.kotlin.web.stack.documentation.models.DocumentedHandler
import com.tobiaswalle.kotlin.web.stack.documentation.models.DocumentedResponse
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
      paths =
        createPaths(options, openApiComponents)
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
          addPathItem(path,
            createPathItem(
              options,
              components,
              metaInfos
            )
          )
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
        val operation = createOperation(
          options,
          components,
          metaInfo
        )
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
    return Operation()
      .summary(createDefaultSummary(metaInfo))
      .operationId(createDefaultOperationId(metaInfo))
      .responses(documentedHandler?.createOpenApiResponses(options, components))
  }

  private fun createDefaultOperationId(metaInfo: HandlerMetaInfo): String {
    val lowerCaseMethod = metaInfo.httpMethod.toString().toLowerCase()
    val capitalizedPath = splitPath(metaInfo.path).joinToString("") { it.capitalize() }
    return lowerCaseMethod + capitalizedPath
  }

  private fun createDefaultSummary(metaInfo: HandlerMetaInfo): String {
    val capitalizedMethod = metaInfo.httpMethod.toString().toLowerCase().capitalize()
    val path = splitPath(metaInfo.path)
    return (listOf(capitalizedMethod) + path).joinToString(" ") { it.trim() }
  }

  private fun splitPath(path: String) = path.split("/").filter { it.isNotEmpty() }

  private fun DocumentedHandler.createOpenApiResponses(
    options: DocumentationOptions,
    components: OpenApiComponents
  ): ApiResponses {
    val documentedHandler = this
    val apiResponses = ApiResponses()
    documentedHandler
      .responses
      .forEach { (name, response) ->
        apiResponses.addApiResponse(
          name,
          response.createOpenApiResponse(options, components)
        )
      }
    return apiResponses
  }

  private fun DocumentedResponse.createOpenApiResponse(
    options: DocumentationOptions,
    components: OpenApiComponents
  ): ApiResponse {
    val objectMapper = options.objectMapper
    val response = this
    val (returnTypeSchema, newSchemas) = response.returnType.asOpenApiSchema(objectMapper)
    components.addSchemas(newSchemas)
    return ApiResponse()
      .description("")
      .content(
        Content()
          .addMediaType(
            "application/json", MediaType()
              .schema(returnTypeSchema)
          )
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
