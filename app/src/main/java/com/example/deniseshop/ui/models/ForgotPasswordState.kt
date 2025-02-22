package com.example.deniseshop.ui.models

data class ForgotPasswordState(
	val email: String = "",
	val emailError: String? = null,
	val isLoading: Boolean = false
)
