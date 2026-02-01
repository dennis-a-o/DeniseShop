package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailDto(
	@SerialName("product") val product: ProductDto,
	@SerialName("review_stat") val reviewStat: ReviewStatDto,
	@SerialName("reviews") val reviews: List<ReviewDto>
)
