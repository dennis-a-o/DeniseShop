package com.example.deniseshop.data.models

import com.example.deniseshop.data.api.BooleanType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiAddress(
	@field:Json(name = "id") val id: Long,
	@field:Json(name = "user_id") val useId: Long,
	@field:Json(name = "name") val name: String,
	@field:Json(name = "email") val email: String,
	@field:Json(name = "phone") val phone: String,
	@field:Json(name = "country") val country: String,
	@field:Json(name = "state") val state: String,
	@field:Json(name = "city") val city: String,
	@field:Json(name = "address") val address: String,
	@field:Json(name = "zip_code") val zipCode: String,
	@field:Json(name = "type") val type: String,
	@BooleanType @field:Json(name = "default") val default: Boolean
)
