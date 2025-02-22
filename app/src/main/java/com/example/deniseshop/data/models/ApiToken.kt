package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiToken(
	@field:Json(name = "refresh_token") val refreshToken: String?,
	@field:Json(name = "access_token") val accessToken: String?,
)
