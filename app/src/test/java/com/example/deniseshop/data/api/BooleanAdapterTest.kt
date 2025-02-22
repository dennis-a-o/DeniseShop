package com.example.deniseshop.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Test

@JsonClass(generateAdapter = true)
data class HelloData(
	@BooleanType
	@field: Json(name = "hello")
	val hello: Boolean,
	@BooleanType
	@field: Json(name = "world")
	val world: Boolean
)
@JsonClass(generateAdapter = true)
data class TestData(
	@field: Json(name = "star")
	val star: Map<String, Int>
)

class BooleanAdapterTest {
	private val moshi = Moshi.Builder()
		.add(BooleanAdapter())
		.add(KotlinJsonAdapterFactory())
		.build()

	@Test
	fun parseJsonToMap(){
		val json = """
			{
			"star": {
				"1": 0,
				"2": 0,
				"3": 0,
				"4": 0,
				"5": 1
            }
			}
		""".trimIndent()

		val helloDataAdapter = moshi.adapter(TestData::class.java)
		val data = helloDataAdapter.fromJson(json)
		val expected = TestData( star = mapOf("1" to 0, "2" to 0, "3" to 0 , "4" to 0, "5" to 1))
		assert(data == expected )
	}

	@Test
	fun `parse Int  0 and 1 To Boolean`(){
		val json = """
			{
			"hello": 1,
			"world": 0
			}
		""".trimIndent()

		val helloDataAdapter = moshi.adapter(HelloData::class.java)
		val data = helloDataAdapter.fromJson(json)
		val expected = HelloData( hello = true, world = false)
		assert(data == expected )
	}

	@Test
	fun `parse string true and false to Boolean`(){
		val json = """
			{
			"hello": "true",
			"world": "0"
			}
		""".trimIndent()

		val helloDataAdapter = moshi.adapter(HelloData::class.java)
		val data = helloDataAdapter.fromJson(json)
		val expected = HelloData( hello = true, world = false)
		assert(data == expected )
	}
}