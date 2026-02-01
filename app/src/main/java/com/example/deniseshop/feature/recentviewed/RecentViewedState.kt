package com.example.deniseshop.feature.recentviewed

import com.example.deniseshop.core.presentation.UiText

data class RecentViewedState(
	val error: UiText? = null,
	val isClearing: Boolean = false,
	val clearSuccess: Boolean =false,
)
