package com.example.deniseshop.feature.signup.presentation

import com.example.deniseshop.core.presentation.UiText

data class SignUpState(
	val firstName: String = "",
	val firstNameError: UiText? = null,
	val lastName: String = "",
	val lastNameError: UiText? = null,
	val email: String = "",
	val emailError: UiText? = null,
	val phone: String = "",
	val phoneError: UiText? = null,
	val password: String = "",
	val passwordError: UiText? = null,
	val acceptTerms: Boolean = false,
	val acceptTermsError: UiText? = null,
	val error: UiText? = null,
	val success: Boolean = false,
	val isLoading: Boolean = false
)
