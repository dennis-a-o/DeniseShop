package com.example.deniseshop.core.domain.model

data class CartProduct(
	val id: Long,
	val productId: Long,
	val userId: Long,
	val name: String,
	val image: String,
	val price: String,
	val activePrice: String,
	val percentageDiscount: Float,
	val quantity: Int,
	val totalPrice:String,
	val color: String?,
	val size: String?
)
