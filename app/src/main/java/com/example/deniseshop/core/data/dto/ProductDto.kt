package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
	@SerialName( "id") val id: Long,
	@SerialName( "name") val name: String,
	@SerialName( "price") val price: String,
	@SerialName( "active_price") val activePrice: String,
	@SerialName( "percentage_discount") val percentageDiscount: Float,
	@SerialName( "image") val image: String,
	@SerialName( "quantity") val quantity: Int,
	@SerialName( "average_rating") val averageRating: Float,
	@SerialName( "review_count") val reviewCount: Int,
	@SerialName( "categories") val categories: List<CategoryDto>? = null,
	@SerialName( "brand") val brand: BrandDto? = null,
	@SerialName( "gallery") val gallery: List<String>? = null,
	@SerialName( "description") val description: String? = null,
	@SerialName( "description_summary") val descriptionSummary: String? = null,
	@SerialName( "size") val size: List<String>?  = null,
	@SerialName( "color") val color: List<String>? = null
)
