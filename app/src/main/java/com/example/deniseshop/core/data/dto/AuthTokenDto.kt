package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenDto(
	@SerialName("refresh_token") val refreshToken: String?,
	@SerialName( "access_token") val accessToken: String?,
)
