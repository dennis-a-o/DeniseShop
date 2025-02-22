package com.example.deniseshop.data.models

data class ApiProductDetail(
	val product: ApiProduct,
	val reviewStat: ApiReviewStat,
	val reviews: List<ApiReview>
)
