package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiCartProduct(
	@field:Json(name = "id") val id: Long,
	@field:Json(name = "product_id") val productId: Long,
	@field:Json(name = "user_id") val userId: Long,
	@field:Json(name = "name") val name: String,
	@field:Json(name = "image") val image: String,
	@field:Json(name = "price") val price: String,
	@field:Json(name = "active_price") val activePrice: String,
	@field:Json(name = "percentage_discount") val percentageDiscount: Float,
	@field:Json(name = "quantity") val quantity: Int,
	@field:Json(name = "total_price") val totalPrice:String,
	@field:Json(name = "color") val color: String?,
	@field:Json(name = "size") val size: String?,
)

@JsonClass(generateAdapter = true)
data class ApiCart(
	@field:Json(name = "cart_items") val cartItems: List<ApiCartProduct>,
	@field:Json(name = "total_price") val totalPrice: String,
	@field:Json(name = "coupon_discount") val couponDiscount: String,
	@field:Json(name = "coupon_type") val couponType: String?,
	@field:Json(name = "coupon") val coupon: String?
)
