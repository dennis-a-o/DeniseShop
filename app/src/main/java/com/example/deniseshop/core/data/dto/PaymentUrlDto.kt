package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentUrlDto(
	@SerialName( "url") val url: String
)
