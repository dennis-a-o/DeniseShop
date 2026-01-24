package com.example.deniseshop.core.domain.model

data class Address(
	val id: Long,
	val useId: Long,
	val name: String,
	val email: String,
	val phone: String,
	val country: String,
	val state: String,
	val city: String,
	val address: String,
	val zipCode: String,
	val type: AddressType,
	val default: Boolean
)
