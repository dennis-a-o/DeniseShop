package com.example.deniseshop.feature.forgotpassword.presentation

sealed class ForgotPasswordEvent {
	data class EmailChange(val email: String): ForgotPasswordEvent()
	data object ResetState: ForgotPasswordEvent()
	data object ForgotPassword: ForgotPasswordEvent()
}