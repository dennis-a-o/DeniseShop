package com.example.deniseshop.ui.models

data class ChangePasswordState(
	val currentPassword: String = "",
	val currentPasswordError: String? = null,
	val currentPasswordVisible: Boolean = false,
	val newPassword: String = "",
	val newPasswordError: String? = null,
	val newPasswordVisible: Boolean = false,
	val isLoading: Boolean = false,
	val isError: Boolean = false,
	val isSuccess: Boolean = false,
	val message: String = "",
)
