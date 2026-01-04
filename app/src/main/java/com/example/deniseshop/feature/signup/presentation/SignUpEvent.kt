package com.example.deniseshop.feature.signup.presentation

sealed class SignUpEvent {
	data class FirstNameChange(val firstName: String): SignUpEvent()
	data class LastNameChange(val lastName: String): SignUpEvent()
	data class EmailChange(val email: String): SignUpEvent()
	data class PhoneChange(val phone: String): SignUpEvent()
	data class PasswordChange(val password: String): SignUpEvent()
	data object ResetState: SignUpEvent()
	data class AcceptTermChange(val accept: Boolean): SignUpEvent()
	data object SignUp: SignUpEvent()
}