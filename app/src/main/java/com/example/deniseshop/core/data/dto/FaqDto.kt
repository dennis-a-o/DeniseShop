package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FaqDto(
	@SerialName( "id") val id: Long,
	@SerialName( "question") val question: String,
	@SerialName( "answer") val answer: String
)
