package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageDto(
	@SerialName( "id") val id: Long,
	@SerialName(  "name") val name: String,
	@SerialName(  "image") val image: String?,
	@SerialName(  "description") val description: String?,
	@SerialName(  "content") val content: String
)
