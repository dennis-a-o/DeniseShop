package com.example.deniseshop.core.domain.model

data class PaymentMethod(
	val id: Long,
	val name: String,
	val slug: String,
	val logo: String,
)