package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiUser(
	@field:Json(name = "id") val id: Long?,
	@field:Json(name = "first_name") val firstName: String?,
	@field:Json(name = "last_name") val lastName: String?,
	@field:Json(name = "email") val email: String?,
	@field:Json(name = "phone") val phone: String?,
	@field:Json(name = "image") val image: String?
)
