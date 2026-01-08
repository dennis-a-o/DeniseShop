package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeaturedFlashSaleDto(
	@SerialName( "flash_sale") val flashSale: FlashSaleDto,
	@SerialName( "products") val products: List<ProductDto>
)
