package com.example.deniseshop.ui.models

data class UiCartProduct(
	val id: Long,
	val productId: Long,
	val userId: Long,
	val name: String,
	val image: String,
	val price: String,
	val percentageDiscount: Float,
	val activePrice: String,
	val quantity: Int,
	val totalPrice:String,
	val color: String?,
	val size: String?,
)

data class UiCart(
	val cartItems: List<UiCartProduct>,
	val totalPrice: String,
	val couponDiscount: String,
	val couponType: String?,
	val coupon: String?
)
