package com.example.deniseshop.feature.changepassword.presentation

sealed class ChangePasswordEvent {
	data class CurrentPasswordChange(val password: String): ChangePasswordEvent()
	data class NewPasswordChange(val password: String): ChangePasswordEvent()
	data object ResetErrorSuccessState: ChangePasswordEvent()
	data object ChangePassword: ChangePasswordEvent()
}