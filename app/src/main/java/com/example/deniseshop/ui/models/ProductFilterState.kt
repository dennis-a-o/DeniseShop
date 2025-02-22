package com.example.deniseshop.ui.models

data class ProductFilterState(
	val selectedCategories: List<String> = emptyList(),
	val selectedBrands: List<String> = emptyList(),
	val selectedColors: List<String> = emptyList(),
	val selectedSize: List<String> = emptyList(),
	val rating: Int = 0,
	val priceRange: ClosedFloatingPointRange<Float> = 0f..9000000f
)
