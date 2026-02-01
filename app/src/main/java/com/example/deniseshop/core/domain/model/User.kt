package com.example.deniseshop.core.domain.model

data class User(
	val id: Long = 0,
	val firstName: String = "",
	val lastName: String = "",
	val email: String = "",
	val phone: String = "",
	val image: String = ""
)
