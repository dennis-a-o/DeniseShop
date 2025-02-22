package com.example.deniseshop.ui.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class UiHome(
	val sliders : List<UiSlider>,
	val categories: List<UiCategory>,
	val featuredFlashSale: UiFeaturedFlashSale?,
	val featured: List<UiProduct>,
	val brands: List<UiBrand>,
	val recentViewed: List<UiProduct>,
	val newArrival: List<UiProduct>,
)

@JsonClass(generateAdapter = true)
data class UiFeaturedFlashSale(
	@field:Json(name = "flash_sale") val flashSale: UiFlashSale,
	@field:Json(name = "products") val products: List<UiProduct>
)

data class UiSlider(
	val id : Long,
	val title: String,
	val subTitle: String,
	val highlightText: String,
	val image: String,
	val description: String,
	val type: String,
	val typeId: Long,
	val link: String,
	val buttonText: String
)

data class UiFlashSale(
	val id: Long,
	val name: String,
	val image: String,
	val description: String?,
	val endDate: String
)

