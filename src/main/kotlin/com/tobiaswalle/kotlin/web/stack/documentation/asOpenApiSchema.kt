package com.tobiaswalle.kotlin.web.stack.documentation

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator
import io.swagger.v3.oas.models.media.Schema
import kotlin.reflect.KClass

fun KClass<*>.asOpenApiSchema(objectMapper: ObjectMapper) = this.java.asOpenApiSchema(objectMapper)

fun Class<*>.asOpenApiSchema(objectMapper: ObjectMapper): Pair<String, Map<String, Schema<*>>> {
  val schemaGenerator = JsonSchemaGenerator(objectMapper)
  val classSchema = schemaGenerator.generateJsonSchema(this) as ObjectNode

  val mainOpenApiSchema = classSchema.asSchema()
  val objectName = this.simpleName
  val subDefinitions = (classSchema.get("definitions") as ObjectNode?)?.fieldsAsSchema() ?: mapOf()
  val definitions = mapOf(objectName to mainOpenApiSchema) + subDefinitions
  val mainRef = "#/definitions/$objectName".fixReference()
  return Pair(mainRef, definitions)
}


private fun JsonNode.asSchema(): Schema<*> {
  return Schema<Any>()
    .properties(this.get("properties")?.let { (it as ObjectNode).fieldsAsSchema() })
    .additionalProperties(this.get("additionalProperties")?.booleanValue())
    .type(this.get("type")?.textValue())
    .format(this.get("format")?.textValue())
    .required(this.get("required")?.map { it.textValue() })
    .`$ref`(this.get("\$ref")?.textValue()?.fixReference())
}

private fun ObjectNode.fieldsAsSchema(): Map<String, Schema<*>> {
  return this.fieldNames()
    .asSequence()
    .map { fieldName -> fieldName to this[fieldName].asSchema() }
    .toMap()

}

private fun String.fixReference(): String {
  return replace("#/definitions/", "#/components/schemas/")
}
