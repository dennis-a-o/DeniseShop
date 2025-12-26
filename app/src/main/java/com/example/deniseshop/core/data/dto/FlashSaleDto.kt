package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FlashSaleDto(
	@SerialName( "id") val id: Long,
	@SerialName( "name") val name: String,
	@SerialName( "image") val image: String,
	@SerialName( "description") val description: String?,
	@SerialName( "end_date") val endDate: String
)
