package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckoutDto(
	@SerialName( "items") val items: List<CartProductDto>,
	@SerialName("address") val address: AddressDto?,
	@SerialName( "payment_methods") val payment: List<PaymentMethodDto>,
	@SerialName( "contain_physical_item")val containPhysicalItem: Boolean,
	@SerialName( "sub_total") val subTotal: String,
	@SerialName( "coupon_discount") val couponDiscount: String?,
	@SerialName( "shipping_fee") val shippingFee: String?,
	@SerialName( "tax") val tax: String?,
	@SerialName( "total_amount") val totalAmount: String
)
