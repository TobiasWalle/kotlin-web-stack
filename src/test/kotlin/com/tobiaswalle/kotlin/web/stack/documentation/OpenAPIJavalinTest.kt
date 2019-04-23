package com.tobiaswalle.kotlin.web.stack.documentation

import com.tobiaswalle.kotlin.web.stack.testing.objectMapper
import com.tobiaswalle.kotlin.web.stack.testing.objectWriter
import io.javalin.Javalin
import io.swagger.v3.oas.models.*
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OpenAPIJavalinTest {
  private class User(val name: String)

  @Test
  fun `createSchema should work with simple schema`() {
    val app = Javalin.create()
    app.get("/user", documented {
      it.json(User(name = "Jim"))
    }.respondWith<User>("200"))
    app.put("/user", documented { })

    val options = DocumentationOptions(
      version = "1.0.0",
      title = "Example",
      javalin = app,
      objectMapper = objectMapper
    )
    val userSchema = Schema<Any>()
      .type("object")
      .addProperties("name", Schema<Any>().type("string"))
      .required(listOf("name"))
      .additionalProperties(false)

    val expectedGetUserOperation = Operation()
      .responses(
        ApiResponses().addApiResponse(
          "200", ApiResponse()
            .description("")
            .content(
              Content()
                .addMediaType(
                  "application/json", MediaType()
                    .schema(Schema<Any>().`$ref`("#/components/schemas/User"))
                )
            )
        )

      )
    val expectedPutUserOperation = Operation()
      .responses(
        ApiResponses().addApiResponse(
          "200",
          ApiResponse().description("")
        )
      )
    val expectedOpenApi = OpenAPI()
      .info(
        Info()
          .version("1.0.0")
          .title("Example")
      )
      .paths(
        Paths().addPathItem(
          "/user",
          PathItem()
            .get(expectedGetUserOperation)
            .put(expectedPutUserOperation)
        )
      )
      .components(Components()
        .schemas(mapOf(
          "User" to userSchema
        ))
      )

    val actual = OpenApiJavalin.createSchema(options)

    println(objectWriter.writeValueAsString(actual))
    assertThat(objectWriter.writeValueAsString(actual)).isEqualTo(objectWriter.writeValueAsString(expectedOpenApi))
  }
}
