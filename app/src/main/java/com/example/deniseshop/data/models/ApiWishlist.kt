package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiWishlist(
	@field:Json(name = "id") val id: Long,
	@field:Json(name = "product_id") val productId: Long,
	@field:Json(name = "image") val image: String,
	@field:Json(name = "name") val name: String,
	@field:Json(name = "price") val price: String,
	@field:Json(name = "active_price") val activePrice: String,
	@field:Json(name = "percentage_discount") val percentageDiscount: Float,
	@field:Json(name = "quantity") val quantity: Int
)
