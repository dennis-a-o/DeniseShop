package com.example.deniseshop.core.domain.model

data class Product(
	val id: Long,
	val name: String,
	val price: String,
	val activePrice: String,
	val percentageDiscount: Float,
	val image: String,
	val quantity: Int,
	val averageRating: Float,
	val reviewCount: Int,
	val categories: List<Category>? = null,
	val brand: Brand? = null,
	val gallery: List<String>? = null,
	val description: String? = null,
	val descriptionSummary: String? = null,
	val size: List<String>?  = null,
	val color: List<String>? = null
)