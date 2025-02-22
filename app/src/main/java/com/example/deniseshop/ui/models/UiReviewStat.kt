package com.example.deniseshop.ui.models

data class UiReviewStat(
	val totalReview: Int,
	val averageRating: Float,
	val starCount: Map<String, Int>
)
