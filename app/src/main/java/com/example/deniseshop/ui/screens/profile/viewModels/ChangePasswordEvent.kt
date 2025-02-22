package com.example.deniseshop.ui.screens.profile.viewModels

sealed class ChangePasswordEvent {
	data class CurrentPasswordChanged(val currentPassword: String): ChangePasswordEvent()
	data class NewPasswordChanged(val newPassword: String): ChangePasswordEvent()
	data class CurrentPasswordVisible(val currentPasswordVisible: Boolean): ChangePasswordEvent()
	data class NewPasswordVisible(val newPasswordVisible: Boolean): ChangePasswordEvent()
	data object Reset: ChangePasswordEvent()
	data object Submit: ChangePasswordEvent()
}