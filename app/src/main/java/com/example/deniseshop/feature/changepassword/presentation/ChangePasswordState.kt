package com.example.deniseshop.feature.changepassword.presentation

import com.example.deniseshop.core.presentation.UiText

data class ChangePasswordState(
	val currentPassword: String = "",
	val currentPasswordError: UiText? = null,
	val newPassword: String = "",
	val newPasswordError: UiText? = null,
	val isLoading: Boolean = false,
	val error: UiText? = null,
	val success: Boolean = false
)