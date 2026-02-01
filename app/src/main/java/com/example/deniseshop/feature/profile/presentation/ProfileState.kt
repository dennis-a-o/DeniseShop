package com.example.deniseshop.feature.profile.presentation

import com.example.deniseshop.core.domain.model.User
import com.example.deniseshop.core.presentation.UiText

data class ProfileState(
	val user: User? = null,
	val isRefreshing: Boolean = false,
	val error: UiText? = null
)
