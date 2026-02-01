package com.example.deniseshop.core.domain.model

data class ProductDetail(
	val product: Product,
	val reviewStat: ReviewStat,
	val reviews: List<Review>
)