package com.example.deniseshop.feature.profile.presentation

sealed class ProfileEvent {
	data object Logout: ProfileEvent()
	data object DeleteAccount: ProfileEvent()
	data object ResetErrorState: ProfileEvent()
	data object Refresh: ProfileEvent()
}