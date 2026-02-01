package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
	@SerialName("id") val id: Long,
	@SerialName("user_id") val useId: Long,
	@SerialName( "name") val name: String,
	@SerialName( "email") val email: String,
	@SerialName("phone") val phone: String,
	@SerialName( "country") val country: String,
	@SerialName( "state") val state: String,
	@SerialName( "city") val city: String,
	@SerialName(  "address") val address: String,
	@SerialName( "zip_code") val zipCode: String,
	@SerialName(  "type") val type: String,
	@SerialName(  "default") val default: Boolean
)
