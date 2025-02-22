package com.example.deniseshop.ui.models

data class SignUpState(
	val firstName: String = "",
	val firstNameError: String? = null,
	val lastName: String = "",
	val lastNameError: String? = null,
	val email: String = "",
	val emailError: String? = null,
	val phone: String = "",
	val phoneError: String? = null,
	val password: String = "",
	val passwordError: String? = null,
	val acceptTerms: Boolean = false,
	val acceptTermsError: String? = null,
	val isVisiblePassword: Boolean = false,
	val isLoading: Boolean = false
)
