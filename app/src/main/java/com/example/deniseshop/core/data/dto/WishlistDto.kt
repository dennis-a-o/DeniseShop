package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WishlistDto(
	@SerialName("id") val id: Long,
	@SerialName( "product_id") val productId: Long,
	@SerialName( "image") val image: String,
	@SerialName( "name") val name: String,
	@SerialName( "price") val price: String,
	@SerialName( "active_price") val activePrice: String,
	@SerialName( "percentage_discount") val percentageDiscount: Float,
	@SerialName( "quantity") val quantity: Int
)
