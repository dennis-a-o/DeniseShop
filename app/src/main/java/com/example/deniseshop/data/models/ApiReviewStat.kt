package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiReviewStat(
	@field:Json(name = "total_review") val totalReview: Int,
	@field:Json(name = "average_rating") val averageRating: Float,
	@field:Json(name = "star_count") val starCount: Map<String, Int>
)
