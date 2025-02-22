package com.example.deniseshop.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiFaq(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "question") val question: String,
    @field:Json(name = "answer") val answer: String
)
