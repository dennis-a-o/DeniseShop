package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiContact(
	@field:Json(name = "contact") val contact: String,
	@field:Json(name = "type") val type: String,
	@field:Json(name = "description") val description: String
)
