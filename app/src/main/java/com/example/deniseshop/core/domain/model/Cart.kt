package com.example.deniseshop.core.domain.model

class Cart(
	val cartItems: List<CartProduct>,
	val totalPrice: String,
	val couponDiscount: String,
	val couponType: String?,
	val coupon: String?
)