package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiReview(
	@field:Json(name = "id") val id: Long,
	@field:Json(name = "reviewer_name") val reviewerName: String,
	@field:Json(name = "reviewer_image") val reviewerImage: String,
	@field:Json(name = "star") val star: Int,
	@field:Json(name = "comment") val comment: String,
	@field:Json(name = "created_at") val createdAt: String
)
