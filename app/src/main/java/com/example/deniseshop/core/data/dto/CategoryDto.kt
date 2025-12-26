package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
	@SerialName( "id") val id: Long,
	@SerialName( "parent_id") val parentId: Long,
	@SerialName(  "name") val name: String,
	@SerialName(  "image") val image: String?,
	@SerialName(  "icon") val icon: String?,
	@SerialName(  "categories") val categories: List<CategoryDto>?,
	@SerialName(  "brands") val brands: List<BrandDto>?
)
