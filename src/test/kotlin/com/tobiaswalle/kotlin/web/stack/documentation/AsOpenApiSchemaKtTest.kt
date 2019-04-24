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

class ObjectWithList(
  val items: List<SubObject>
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

    assertThat(mainRef).isEqualTo(Schema<Any>().`$ref`("#/components/schemas/MyObject"))
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

  @Test
  fun `Class#asOpenApiSchema should work with arrays`() {
    val subObjectSchema = ObjectSchema()
      .additionalProperties(false)
      .properties(
        mutableMapOf(
          "a" to Schema<Any>().type("integer")
        )
      )
      .required(listOf("a"))
    val expectedDefinitions = mapOf(
      "SubObject" to subObjectSchema
    )
    val subObjectSchemaRef = Schema<Any>().`$ref`("#/components/schemas/SubObject")
    val (mainRef, actualDefinitions) = Array<SubObject>::class.asOpenApiSchema(objectMapper)

    assertThat(
      objectWriter.writeValueAsString(
        mainRef
      )
    ).isEqualTo(
      objectWriter.writeValueAsString(
        ArraySchema().items(subObjectSchemaRef)
      )
    )
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

  @Test
  fun `Class#asOpenApiSchema should work with arrays in objects`() {
    val subObjectSchema = ObjectSchema()
      .additionalProperties(false)
      .properties(
        mutableMapOf(
          "a" to Schema<Any>().type("integer")
        )
      )
      .required(listOf("a"))
    val subObjectSchemaRef = Schema<Any>().`$ref`("#/components/schemas/SubObject")
    val objectWithListSchema = ObjectSchema()
      .additionalProperties(false)
      .properties(
        mutableMapOf(
          "items" to ArraySchema().items(subObjectSchemaRef)
        )
      )
      .required(listOf("items"))
    val expectedDefinitions = mapOf(
      "ObjectWithList" to objectWithListSchema,
      "SubObject" to subObjectSchema

    )
    val (mainRef, actualDefinitions) = ObjectWithList::class.asOpenApiSchema(objectMapper)

    assertThat(
      objectWriter.writeValueAsString(
        mainRef
      )
    ).isEqualTo(
      objectWriter.writeValueAsString(
        Schema<Any>().`$ref`("#/components/schemas/ObjectWithList")
      )
    )
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
