package com.example.deniseshop.ui.models

data class UiOrder(
	val id: Long,
	val name: String,
	val image: String,
	val code: String,
	val status: String,
	val date: String
)

data class UiOrderProduct(
	val id: Long,
	val productId: Long,
	val name: String,
	val image: String,
	val price: String,
	val quantity: Int,
	val size: String?,
	val color: String?,
	val rated: Boolean,
	val downloadable: Boolean
)

data class UiOrderAddress(
	val id: Long,
	val name: String,
	val email: String,
	val phone: String,
	val address: String,
)

data class UiOrderDetail(
	val id: Long,
	val code: String,
	val status: String,
	val paymentMethod: String,
	val paymentStatus: String,
	val amount: String,
	val tax: String,
	val shippingFee: String,
	val discount: String,
	val products: List<UiOrderProduct>,
	val orderAddress: UiOrderAddress?,
	val date: String,
	val shippingStatus: String?,
	val shipping: String?,
	val containPhysicalProduct: Boolean
)