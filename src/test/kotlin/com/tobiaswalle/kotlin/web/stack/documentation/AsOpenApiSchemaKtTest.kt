package com.tobiaswalle.kotlin.web.stack.documentation

import com.fasterxml.jackson.module.jsonSchema.types.ReferenceSchema
import com.tobiaswalle.kotlin.web.stack.testing.objectMapper
import com.tobiaswalle.kotlin.web.stack.testing.objectWriter
import io.swagger.v3.oas.models.media.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SubObject(
  val a: Int
)

class MyObject(
  val a: String,
  val b: Int,
  val c: Float,
  val d: Boolean?,
  val e: SubObject
)

internal class AsOpenApiSchemaKtTest {
  @Test
  fun `Class#asOpenApiSchema should work with objects`() {
    val subObjectSchema = ObjectSchema()
      .additionalProperties(false)
      .properties(
        mutableMapOf(
          "a" to Schema<Any>().type("integer")
        )
      )
      .required(listOf("a"))
    val myObjectSchema = ObjectSchema()
      .additionalProperties(false)
      .properties(
        mutableMapOf(
          "a" to StringSchema(),
          "b" to Schema<Any>().type("integer"),
          "c" to NumberSchema(),
          "d" to BooleanSchema(),
          "e" to ReferenceSchema("#/components/schemas/SubObject")
        )
      )
      .required(listOf("a", "b", "c", "e"))
    val expectedDefinitions = mapOf(
      "MyObject" to myObjectSchema,
      "SubObject" to subObjectSchema
    )
    val (mainRef, actualDefinitions) = MyObject::class.asOpenApiSchema(objectMapper)

    assertThat(mainRef).isEqualTo("#/components/schemas/MyObject")
    assertThat(
      objectWriter.writeValueAsString(
        actualDefinitions
      )
    ).isEqualTo(
      objectWriter.writeValueAsString(
        expectedDefinitions
      )
    )
  }
}
