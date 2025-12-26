package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderDetailDto(
	@SerialName( "id") val id: Long,
	@SerialName( "code") val code: String,
	@SerialName( "status") val status: String,
	@SerialName( "payment_method") val paymentMethod: String,
	@SerialName( "payment_status") val paymentStatus: String,
	@SerialName( "amount") val amount: String,
	@SerialName( "tax") val tax: String,
	@SerialName( "shipping_fee") val shippingFee: String,
	@SerialName( "discount") val discount: String,
	@SerialName( "products") val products: List<OrderProductDto>,
	@SerialName( "order_address") val orderAddress: OrderAddressDto?,
	@SerialName( "shipping_status") val shippingStatus: String?,
	@SerialName( "shipping") val shipping: String?,
	@SerialName( "date") val date: String,
	@SerialName( "contain_physical_product") val containPhysicalProduct:Boolean
)
