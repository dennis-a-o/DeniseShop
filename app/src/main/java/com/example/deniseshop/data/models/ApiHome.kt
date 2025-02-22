package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiFeaturedFlashSale(
	@field:Json(name = "flash_sale") val flashSale: ApiFlashSale,
	@field:Json(name = "products") val products: List<ApiProduct>
)

@JsonClass(generateAdapter = true)
data class ApiHome(
	@field:Json(name = "sliders") val sliders : List<ApiSlider>,
	@field:Json(name = "categories") val categories: List<ApiCategory>,
	@field:Json(name = "featured_flash_sale") val featuredFlashSale: ApiFeaturedFlashSale?,
	@field:Json(name = "featured") val featured: List<ApiProduct>,
	@field:Json(name = "brands") val brands: List<ApiBrand>,
	@field:Json(name = "recent_viewed") val recentViewed: List<ApiProduct>,
	@field:Json(name = "new_arrival") val newArrival: List<ApiProduct>,
)

@JsonClass(generateAdapter = true)
data class ApiSlider(
	@field:Json(name = "id") val id : Long,
	@field:Json(name = "title") val title: String,
	@field:Json(name = "sub_title") val subTitle: String,
	@field:Json(name = "image") val image: String,
	@field:Json(name = "highlight_text") val highlightText: String,
	@field:Json(name = "description") val description: String,
	@field:Json(name = "link") val link: String,
	@field:Json(name = "type") val type: String,
	@field:Json(name = "type_id") val typeId: Long,
	@field:Json(name = "button_text") val buttonText: String
)

@JsonClass(generateAdapter = true)
data class ApiFlashSale(
	@field:Json(name = "id") val id: Long,
	@field:Json(name = "name") val name: String,
	@field:Json(name = "image") val image: String,
	@field:Json(name = "description") val description: String?,
	@field:Json(name = "end_date") val endDate: String
)

