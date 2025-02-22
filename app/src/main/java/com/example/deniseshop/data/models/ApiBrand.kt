package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiBrand(
	@field:Json(name = "id") val id: Long,
	@field:Json(name = "name") val name: String,
	@field:Json(name = "logo") val logo: String
)