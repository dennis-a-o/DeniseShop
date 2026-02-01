package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeDto(
	@SerialName("sliders") val sliders : List<SliderDto>,
	@SerialName("categories") val categories: List<CategoryDto>,
	@SerialName("featured_flash_sale") val featuredFlashSale: FeaturedFlashSaleDto?,
	@SerialName("featured") val featured: List<ProductDto>,
	@SerialName("brands") val brands: List<BrandDto>,
	@SerialName("recent_viewed") val recentViewed: List<ProductDto>,
	@SerialName("new_arrival") val newArrival: List<ProductDto>,
)
