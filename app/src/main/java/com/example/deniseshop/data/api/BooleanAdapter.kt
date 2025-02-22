package com.example.deniseshop.data.api

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson


@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class BooleanType

class BooleanAdapter {
	@BooleanType
	@FromJson
	fun fromJson(reader: JsonReader): Boolean {
		return  when(reader.peek()){
			JsonReader.Token.NULL -> false
			JsonReader.Token.NUMBER -> when(val value = reader.nextInt()){
				0 -> false
				1 -> true
				else -> throw JsonDataException("Number $value cannot be transformed to  a Boolean")
			}
			JsonReader.Token.STRING -> when(val value = reader.nextString()){
				"false" -> false
				"true" -> true
				"1" -> true
				"0" -> false
				else -> throw JsonDataException("String $value cannot be transformed to  a Boolean")
			}
			JsonReader.Token.BOOLEAN -> reader.nextBoolean()
			else -> throw JsonDataException("Expected number but was ${reader.peek()}")
		}
	}

	@ToJson
	fun toJson(writer: JsonWriter, @BooleanType p1: Boolean){
		writer.value(if(p1) 1 else 0)
	}
}