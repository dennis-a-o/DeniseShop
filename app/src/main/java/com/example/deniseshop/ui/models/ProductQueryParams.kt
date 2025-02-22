package com.example.deniseshop.ui.models

data class ProductQueryParams(
	val page: Int = 0,
	val pageSize: Int = 20,
	val query: String = "",
	val minPrice: Int = 0,
	val maxPrice: Int = 0,
	val sortBy: String = "dateDesc",
	val categories: List<String> = emptyList(),
	val brands: List<String> = emptyList(),
	val colors: List<String> = emptyList(),
	val sizes: List<String> = emptyList(),
	val rating: Int = 0
)
