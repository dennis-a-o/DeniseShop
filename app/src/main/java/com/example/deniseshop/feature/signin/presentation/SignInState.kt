package com.example.deniseshop.feature.signin.presentation

import com.example.deniseshop.core.presentation.UiText

data class SignInState(
	val email: String = "",
	val emailError: UiText? = null,
	val password: String = "",
	val passwordError: UiText? = null,
	val isVisiblePassword: Boolean = false,
	val isLoading: Boolean = false,
	val error: UiText? = null,
	val success: Boolean = false
)
