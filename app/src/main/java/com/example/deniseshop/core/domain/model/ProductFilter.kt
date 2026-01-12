package com.example.deniseshop.core.domain.model

data class ProductFilter(
	val categories: List<String> = emptyList(),
	val brands: List<String> = emptyList(),
	val colors: List<String> = emptyList(),
	val sizes: List<String> = emptyList(),
	val maxPrice: Long = 1_000_000L,
	val currency: String = "KES"
)