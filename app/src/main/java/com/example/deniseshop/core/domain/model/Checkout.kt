package com.example.deniseshop.core.domain.model


data class Checkout(
 	val items: List<CartProduct>,
	val address: Address?,
	val payment: List<PaymentMethod>,
	val containPhysicalItem: Boolean,
	val subTotal: String,
	val couponDiscount: String?,
	val shippingFee: String?,
	val tax: String?,
	val totalAmount: String
)