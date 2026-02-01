package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContactDto(
	@SerialName("contact") val contact: String,
	@SerialName("type") val type: String,
	@SerialName("description") val description: String
)