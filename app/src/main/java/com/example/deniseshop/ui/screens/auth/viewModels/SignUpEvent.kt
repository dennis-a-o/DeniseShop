package com.example.deniseshop.ui.screens.auth.viewModels

sealed class SignUpEvent {
	data class FirstNameChanged(val firstName: String): SignUpEvent()
	data class LastNameChanged(val lastName: String): SignUpEvent()
	data class EmailChanged(val email: String): SignUpEvent()
	data class PhoneChanged(val phone: String): SignUpEvent()
	data class PasswordChanged(val password: String): SignUpEvent()
	data class VisiblePassword(val visible: Boolean): SignUpEvent()
	data class AcceptTermChanged(val accept: Boolean): SignUpEvent()
	data object Submit: SignUpEvent()
}