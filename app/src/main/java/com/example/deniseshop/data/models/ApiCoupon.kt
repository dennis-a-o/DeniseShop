package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiCoupon(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "code") val code: String,
    @field:Json(name = "value") val value: Int,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "description") val description: String?
)
