package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.ProductDto
import com.example.deniseshop.core.data.dto.ProductFilterDto
import com.example.deniseshop.core.domain.model.Product
import com.example.deniseshop.core.domain.model.ProductFilter

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

fun ProductFilterDto.toProductFilter() = ProductFilter(
	categories = categories ?: emptyList(),
	brands = brands?: emptyList(),
	colors = colors?: emptyList(),
	sizes = sizes?: emptyList(),
	maxPrice = maxPrice ?: 1_000_000L,
	currency = currency ?: "KES"
)