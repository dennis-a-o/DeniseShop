package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse(
	@field:Json(name = "success") val success: Boolean,
	@field:Json(name = "message") val message: String
)