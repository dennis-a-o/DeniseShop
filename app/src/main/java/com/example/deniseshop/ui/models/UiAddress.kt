package com.example.deniseshop.ui.models

import kotlinx.serialization.Serializable

@Serializable
data class UiAddress(
	val id: Long = 0,
	val useId: Long = 0,
	val name: String = "",
	val email: String = "",
	val phone: String = "",
	val country: String = "",
	val state: String = "",
	val city: String = "",
	val address: String = "",
	val zipCode: String ="",
	val  type: String= "",
	val default: Boolean = false
)

