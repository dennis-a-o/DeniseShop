package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiProductFilter(
	@field:Json(name = "categories") val categories: List<String>?,
	@field:Json(name = "brands") val brands: List<String>?,
	@field:Json(name = "colors") val colors: List<String>?,
	@field:Json(name = "sizes") val sizes: List<String>?,
	@field:Json(name = "max_price") val maxPrice: Long?,
	@field:Json(name = "currency")  val currency: String?
)
