package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartDto(
	@SerialName("cart_items") val cartItems: List<CartProductDto>,
	@SerialName( "total_price") val totalPrice: String,
	@SerialName( "coupon_discount") val couponDiscount: String,
	@SerialName( "coupon_type") val couponType: String?,
	@SerialName( "coupon") val coupon: String?
)
