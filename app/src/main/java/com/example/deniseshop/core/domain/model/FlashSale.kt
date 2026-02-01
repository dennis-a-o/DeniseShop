package com.example.deniseshop.core.domain.model

data class FlashSale(
	val id: Long,
	val name: String,
	val image: String,
	val description: String?,
	val endDate: String
)
