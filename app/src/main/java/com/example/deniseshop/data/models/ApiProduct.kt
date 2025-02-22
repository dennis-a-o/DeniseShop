package com.example.deniseshop.data.models

import com.example.deniseshop.data.api.BooleanType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiProduct(
	@field:Json(name = "id") val id: Long,
	@field:Json(name = "name") val name: String,
	@field:Json(name = "price") val price: String,
	@field:Json(name = "active_price") val activePrice: String,
	@field:Json(name = "percentage_discount") val percentageDiscount: Float,
	@field:Json(name = "image") val image: String,
	@field:Json(name = "quantity") val quantity: Int,
	@field:Json(name = "average_rating") val averageRating: Float,
	@field:Json(name = "review_count") val reviewCount: Int,
	@field:Json(name = "categories") val categories: List<ApiCategory>? = null,
	@field:Json(name = "brand") val brand: ApiBrand? = null,
	@field:Json(name = "gallery") val gallery: List<String>? = null,
	@field:Json(name = "description") val description: String? = null,
	@field:Json(name = "description_summary") val descriptionSummary: String? = null,
	@field:Json(name = "size") val size: List<String>?  = null,
	@field:Json(name = "color") val color: List<String>? = null
)