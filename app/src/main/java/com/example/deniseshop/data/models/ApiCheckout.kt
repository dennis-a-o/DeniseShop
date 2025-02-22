package com.example.deniseshop.data.models

import com.example.deniseshop.data.api.BooleanType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiCheckout(
	@field:Json(name = "items") val items: List<ApiCartProduct>,
	@field:Json(name = "address") val address: ApiAddress?,
	@field:Json(name = "payment_methods") val payment: List<ApiPaymentMethod>,
	@BooleanType  @field:Json(name = "contain_physical_item")val containPhysicalItem: Boolean,
	@field:Json(name = "sub_total") val subTotal: String,
	@field:Json(name = "coupon_discount") val couponDiscount: String?,
	@field:Json(name = "shipping_fee") val shippingFee: String?,
	@field:Json(name = "tax") val tax: String?,
	@field:Json(name = "total_amount") val totalAmount: String,
)

@JsonClass(generateAdapter = true)
data class ApiPaymentMethod(
	@field:Json(name = "id") val id: Long,
	@field:Json(name = "name") val name: String,
	@field:Json(name = "slug") val slug: String,
	@field:Json(name = "logo") val logo: String,
)
