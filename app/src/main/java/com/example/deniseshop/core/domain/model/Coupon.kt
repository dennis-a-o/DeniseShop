package com.example.deniseshop.core.domain.model

data class Coupon(
	val id: Long,
	val code: String,
	val value: Int,
	val type: String,
	val description: String?
)