package com.example.deniseshop.ui.screens.auth.viewModels

sealed class SignInEvent {
	data class EmailChanged(val email: String): SignInEvent()
	data class PasswordChanged(val password: String): SignInEvent()
	data class VisiblePassword(val visible: Boolean): SignInEvent()
	data object Submit: SignInEvent()
}