package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CouponDto(
	@SerialName("id") val id: Long,
	@SerialName( "code") val code: String,
	@SerialName(  "value") val value: Int,
	@SerialName(  "type") val type: String,
	@SerialName(  "description") val description: String?
)
