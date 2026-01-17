package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
	@SerialName("message") val message: String
)
