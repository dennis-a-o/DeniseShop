package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderAddressDto(
	@SerialName( "id") val id: Long,
	@SerialName(  "name") val name: String,
	@SerialName(  "email") val email: String,
	@SerialName(  "phone") val phone: String,
	@SerialName(  "address") val address: String,
)
