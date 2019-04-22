package com.tobiaswalle.kotlin.web.stack.documentation

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import io.javalin.Javalin
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OpenAPIJavalinTest {

    private class User(val name: String)

    private class Tweet(val content: String)

    @Test
    fun `createSchema should create an open api schema`() {

        val app = Javalin.create()
        app.get("/users", documented {
            it.json(
                listOf(
                    User(name = "Jim"),
                    User(name = "Tim")
                )
            )
        })
        app.put("/users", documented { })
        app.get("/tweets", documented {
            it.json(
                listOf(
                    Tweet(content = "How are you?")
                )
            )
        })
        app.post("/tweets", documented {})
        val options = DocumentationOptions(
            version = "1.0.0",
            title = "Example",
            javalin = app
        )

        val expectedOpenApi = OpenAPI().apply {
            info = Info().apply {
                version = "1.0.0"
                title = "Example"
            }
            paths = Paths().apply {
                addPathItem("/users", PathItem().apply {
                    get = Operation().apply {
                        responses = ApiResponses().apply {
                            addApiResponse("200", ApiResponse().apply {
                                description = ""
                            })
                        }
                    }
                    put = Operation().apply {
                        responses = ApiResponses().apply {
                            addApiResponse("200", ApiResponse().apply {
                                description = ""
                            })
                        }
                    }
                })
                addPathItem("/tweets", PathItem().apply {
                    get = Operation().apply {
                        responses = ApiResponses().apply {
                            addApiResponse("200", ApiResponse().apply {
                                description = ""
                            })
                        }
                    }
                    post = Operation().apply {
                        responses = ApiResponses().apply {
                            addApiResponse("200", ApiResponse().apply {
                                description = ""
                            })
                        }
                    }
                })
            }
        }

        val objectMapper = ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .writerWithDefaultPrettyPrinter()
        val actual = OpenApiJavalin.createSchema(options)

        assertThat(objectMapper.writeValueAsString(actual)).isEqualTo(objectMapper.writeValueAsString(expectedOpenApi))
    }
}
