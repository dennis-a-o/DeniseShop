package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserCredential(
	@SerialName("auth_token") val authToken: AuthTokenDto,
	@SerialName("user") val user: UserDto
)
