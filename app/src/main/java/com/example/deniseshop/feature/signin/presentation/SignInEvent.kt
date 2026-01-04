package com.example.deniseshop.feature.signin.presentation


sealed class SignInEvent {
	data class EmailChange(val email: String): SignInEvent()
	data class PasswordChange(val password: String): SignInEvent()
	data object ResetState: SignInEvent()
	data object SignIn: SignInEvent()
}