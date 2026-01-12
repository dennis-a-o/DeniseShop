package com.example.deniseshop.core.domain.model

data class ProductFilterParams(
	val page: Int = 0,
	val pageSize: Int = 20,
	val query: String = "",
	val minPrice: Int = 0,
	val maxPrice: Int = 0,
	val sortBy: ProductSortOption = ProductSortOption.DATE_ASCENDING,
	val categories: List<String> = emptyList(),
	val brands: List<String> = emptyList(),
	val colors: List<String> = emptyList(),
	val sizes: List<String> = emptyList(),
	val rating: Int = 0
)
