package com.example.deniseshop.ui.screens.profile.viewModels

sealed class ProfileEvent {
	data object Logout: ProfileEvent()
	data object DeleteProfile: ProfileEvent()
	data class EditUser(val edit: Boolean): ProfileEvent()
	data class ChangePassword(val change: Boolean): ProfileEvent()
	data class ChangeTheme(val change: Boolean): ProfileEvent()
	data object Reset: ProfileEvent()
}