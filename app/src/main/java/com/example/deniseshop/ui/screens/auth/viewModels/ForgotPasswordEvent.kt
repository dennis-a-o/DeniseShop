package com.example.deniseshop.ui.screens.auth.viewModels

sealed class ForgotPasswordEvent {
	data class EmailChanged(val email: String): ForgotPasswordEvent()
	data object Submit: ForgotPasswordEvent()
}