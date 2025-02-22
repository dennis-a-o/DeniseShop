package com.example.deniseshop.ui.models

data class UiProductFilter(
	val categories: List<String>?,
	val brands: List<String>?,
	val colors: List<String>?,
	val sizes: List<String>?,
	val maxPrice: Long?,
	val currency: String?
)
