package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
	@SerialName( "id") val id: Long,
	@SerialName( "name") val name: String,
	@SerialName( "image") val image: String,
	@SerialName( "code") val code: String,
	@SerialName( "status") val status: String,
	@SerialName("created_at") val date: String
)
