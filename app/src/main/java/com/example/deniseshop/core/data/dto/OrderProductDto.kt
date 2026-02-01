package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderProductDto(
	@SerialName( "id") val id: Long,
	@SerialName(  "product_id") val productId: Long,
	@SerialName( "name") val name: String,
	@SerialName(  "image") val image: String,
	@SerialName(  "price") val price: String,
	@SerialName(  "quantity") val quantity: Int,
	@SerialName(  "size") val size: String?,
	@SerialName(  "color") val color: String?,
	@SerialName(  "rated") val rated: Boolean,
	@SerialName(  "downloadable") val downloadable: Boolean?
)
