package com.example.deniseshop.ui.models

data class UiCheckout(
	val items: List<UiCartProduct>,
	val address: UiAddress?,
	val payment: List<UiPaymentMethod>,
	val containPhysicalItem: Boolean,
	val subTotal: String,
	val couponDiscount: String?,
	val shippingFee: String?,
	val tax: String?,
	val totalAmount: String,
)

data class UiPaymentMethod(
	val id: Long,
	val name: String,
	val slug: String,
	val logo: String,
)
