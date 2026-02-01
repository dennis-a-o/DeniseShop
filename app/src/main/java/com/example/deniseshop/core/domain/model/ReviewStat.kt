package com.example.deniseshop.core.domain.model

data class ReviewStat(
	val totalReview: Int,
	val averageRating: Float,
	val starCount: Map<String, Int>
)