package com.example.deniseshop.core.domain.model

data class OrderAddress(
	val id: Long,
	val name: String,
	val email: String,
	val phone: String,
	val address: String,
)