package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPage(
	@field:Json(name = "id") val id: Long,
	@field:Json(name = "name") val name: String,
	@field:Json(name = "image") val image: String?,
	@field:Json(name = "description") val description: String?,
	@field:Json(name = "content") val content: String
)