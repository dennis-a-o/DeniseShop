package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
	@SerialName( "id") val id: Long,
	@SerialName("first_name") val firstName: String,
	@SerialName("last_name") val lastName: String,
	@SerialName( "email") val email: String,
	@SerialName( "phone") val phone: String,
	@SerialName("image") val image: String?
)
