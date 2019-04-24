package com.tobiaswalle.kotlin.web.stack.documentation

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator
import io.swagger.v3.oas.models.media.ArraySchema
import io.swagger.v3.oas.models.media.ObjectSchema
import io.swagger.v3.oas.models.media.Schema
import kotlin.reflect.KClass

fun KClass<*>.asOpenApiSchema(objectMapper: ObjectMapper) = this.java.asOpenApiSchema(objectMapper)

fun Class<*>.asOpenApiSchema(objectMapper: ObjectMapper): Pair<Schema<*>, Map<String, Schema<*>>> {
  val clazz = this
  val schemaGenerator = JsonSchemaGenerator(objectMapper)
  val mainJsonSchema = schemaGenerator.generateJsonSchema(clazz)

  val mainOpenApiSchema = mainJsonSchema.asSchema()
  val subDefinitions = mainJsonSchema.get("definitions")?.fieldsAsSchema() ?: mapOf()
  if (mainJsonSchema.get("type").asText() == "object") {
    val objectName = clazz.simpleName
    val definitions = mapOf(objectName to mainOpenApiSchema) + subDefinitions
    val mainRef = "#/definitions/$objectName".replaceDefinitionsPath()
    return Pair(createRefSchema(mainRef), definitions)
  }
  return Pair(mainOpenApiSchema, subDefinitions)
}

private fun JsonNode.asSchema(): Schema<*> {
  val schema = when (this.get("type")?.asText()) {
    "object" -> ObjectSchema()
    "array" -> ArraySchema().items(this.get("items")?.asSchema())
    else -> Schema<Any>()
  }
  return schema
    .properties(this.get("properties")?.let { (it as ObjectNode).fieldsAsSchema() })
    .additionalProperties(this.get("additionalProperties")?.booleanValue())
    .type(this.get("type")?.textValue())
    .format(this.get("format")?.textValue())
    .required(this.get("required")?.map { it.textValue() })
    .`$ref`(this.get("\$ref")?.textValue()?.replaceDefinitionsPath())
}

private fun JsonNode.fieldsAsSchema(): Map<String, Schema<*>> {
  return this.fieldNames()
    .asSequence()
    .map { fieldName -> fieldName to this[fieldName].asSchema() }
    .toMap()

}

private fun String.replaceDefinitionsPath(): String {
  return replace("#/definitions/", "#/components/schemas/")
}

private fun createRefSchema(ref: String) = Schema<Any>().`$ref`(ref)

