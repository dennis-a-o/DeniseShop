package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiCategory(
	@field:Json(name = "id") val id: Long,
	@field:Json(name = "parent_id") val parentId: Long,
	@field:Json(name = "name") val name: String,
	@field:Json(name = "image") val image: String?,
	@field:Json(name = "icon") val icon: String?,
	@field:Json(name = "categories") val categories: List<ApiCategory>?,
	@field:Json(name = "brands") val brands: List<ApiBrand>?
)