package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartProductDto(
	@SerialName( "id") val id: Long,
	@SerialName( "product_id") val productId: Long,
	@SerialName( "user_id") val userId: Long,
	@SerialName( "name") val name: String,
	@SerialName("image") val image: String,
	@SerialName( "price") val price: String,
	@SerialName( "active_price") val activePrice: String,
	@SerialName( "percentage_discount") val percentageDiscount: Float,
	@SerialName( "quantity") val quantity: Int,
	@SerialName( "total_price") val totalPrice:String,
	@SerialName( "color") val color: String?,
	@SerialName( "size") val size: String?
)
