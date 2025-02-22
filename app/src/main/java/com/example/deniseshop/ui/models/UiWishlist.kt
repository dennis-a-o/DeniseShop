package com.example.deniseshop.ui.models

data class UiWishlist(
	val id: Long,
	val productId: Long,
	val image: String,
	val name: String,
	val price: String,
	val activePrice: String,
	val percentageDiscount: Float,
	val quantity: Int
)
