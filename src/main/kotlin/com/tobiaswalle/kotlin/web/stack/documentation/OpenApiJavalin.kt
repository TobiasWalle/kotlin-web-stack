package com.tobiaswalle.kotlin.web.stack.documentation

import io.javalin.HandlerMetaInfo
import io.javalin.core.HandlerType
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses

object OpenApiJavalin {
    fun createSchema(options: DocumentationOptions): OpenAPI {
        val schema = OpenAPI().apply {
            info = createOpenApiInfo(options)
            paths = createPaths(options)
        }

        return schema
    }

    private fun createOpenApiInfo(options: DocumentationOptions): Info {
        return Info().apply {
            title = options.title
            version = options.version
        }
    }

    private fun createPaths(options: DocumentationOptions): Paths {
        val javalin = options.javalin
        return Paths().apply {
            javalin.handlerMetaInfo
                .groupBy { it.path }
                .forEach { (path, metaInfos) ->
                    addPathItem(path, createPathItem(metaInfos))
                }
        }
    }

    private fun createPathItem(
        metaInfos: List<HandlerMetaInfo>
    ): PathItem {
        return PathItem().apply {
            metaInfos.forEach { metaInfo ->
                val operation = createOperation()
                when (metaInfo.httpMethod) {
                    HandlerType.GET -> get = operation
                    HandlerType.PUT -> put = operation
                    HandlerType.POST -> post = operation
                    HandlerType.DELETE -> delete = operation
                    HandlerType.OPTIONS -> options = operation
                    HandlerType.HEAD -> head = operation
                    HandlerType.PATCH -> patch = operation
                    HandlerType.TRACE -> trace = operation
                    else -> Unit
                }
            }
        }
    }

    private fun createOperation(): Operation {
        return Operation().apply {
            responses = ApiResponses().apply {
                addApiResponse("200", ApiResponse().apply {
                    description = ""
                })
            }
        }
    }
}
