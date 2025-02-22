package com.example.deniseshop.domain.models

data class UserSignUpData(
	val firstName: String,
	val lastName: String,
	val email: String,
	val phone: String,
	val password: String,
	val acceptTerms: Boolean
)
