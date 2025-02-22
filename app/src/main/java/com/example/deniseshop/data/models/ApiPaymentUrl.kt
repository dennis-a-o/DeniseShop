package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPaymentUrl(
	@field:Json(name = "url") val url: String
)
