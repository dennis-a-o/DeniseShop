package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductFilterDto(
	@SerialName("categories") val categories: List<String>?,
	@SerialName( "brands") val brands: List<String>?,
	@SerialName( "colors") val colors: List<String>?,
	@SerialName( "sizes") val sizes: List<String>?,
	@SerialName( "max_price") val maxPrice: Long?,
	@SerialName( "currency")  val currency: String?
)
