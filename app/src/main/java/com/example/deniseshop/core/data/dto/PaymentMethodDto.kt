package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethodDto(
	@SerialName( "id") val id: Long,
	@SerialName( "name") val name: String,
	@SerialName( "slug") val slug: String,
	@SerialName( "logo") val logo: String,
)