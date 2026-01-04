package com.example.deniseshop.feature.forgotpassword.presentation

import com.example.deniseshop.core.presentation.UiText

data class ForgotPasswordState(
	val email: String = "",
	val emailError: UiText? = null,
	val isLoading: Boolean = false,
	val success: Boolean = false,
	val error: UiText? = null
)
