package com.example.deniseshop.ui.models

data class SignInState(
	val email: String = "",
	val emailError: String? = null,
	val password: String = "",
	val passwordError: String? = null,
	val isVisiblePassword: Boolean = false,
	val isLoading: Boolean = false
)
