package com.example.deniseshop.ui.models

data class ProfileState(
	val isLoggedIn: Boolean = false,
	val editUser: Boolean = false,
	val changePassword: Boolean = false,
	val changeTheme: Boolean = false,
	val isLoading: Boolean = false,
	val isError: Boolean = false,
	val isSuccess: Boolean = false,
	val message: String = ""
)
