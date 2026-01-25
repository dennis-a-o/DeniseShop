package com.example.deniseshop.core.domain.model

data class OrderDetail(
	val id: Long,
	val code: String,
	val status: String,
	val paymentMethod: String,
	val paymentStatus: String,
	val amount: String,
	val tax: String,
	val shippingFee: String,
	val discount: String,
	val products: List<OrderProduct>,
	val orderAddress: OrderAddress?,
	val shippingStatus: String?,
	val shipping: String?,
	val date: String,
	val containPhysicalProduct:Boolean
)