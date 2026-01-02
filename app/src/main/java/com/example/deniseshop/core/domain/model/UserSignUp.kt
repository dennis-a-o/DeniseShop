package com.example.deniseshop.core.domain.model

data class UserSignUp(
	val firstName: String,
	val lastName: String,
	val email: String,
	val phone: String,
	val password: String,
	val acceptTerms: Boolean
)

