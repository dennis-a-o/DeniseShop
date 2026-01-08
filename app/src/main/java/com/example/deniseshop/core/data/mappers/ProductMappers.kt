package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.ProductDto
import com.example.deniseshop.core.domain.model.Product

fun ProductDto.toProduct(): Product {
	return Product(
		id = id,
		name = name,
		price = price,
		activePrice = activePrice,
		percentageDiscount = percentageDiscount,
		image = image,
		quantity = quantity,
		averageRating = averageRating,
		reviewCount = reviewCount,
		categories = categories?.map { it.toCategory() },
		brand = brand?.toBrand(),
		gallery = gallery,
		description = description,
		descriptionSummary = descriptionSummary,
		size = size,
		color = color
	)
}