package com.example.deniseshop.core.domain.model

data class Home(
	val sliders : List<Slider>,
	val categories: List<Category>,
	val featuredFlashSale: FeaturedFlashSale?,
	val featured: List<Product>,
	val brands: List<Brand>,
	val recentViewed: List<Product>,
	val newArrival: List<Product>,
)
