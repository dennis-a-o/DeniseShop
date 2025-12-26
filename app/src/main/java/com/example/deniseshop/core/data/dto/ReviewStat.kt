package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewStat(
	@SerialName("total_review") val totalReview: Int,
	@SerialName("average_rating") val averageRating: Float,
	@SerialName( "star_count") val starCount: Map<String, Int>
)
