package com.example.deniseshop.core.presentation

sealed interface ScreenState<out T> {
	data object Loading: ScreenState<Nothing>
	data class Success<T>(val data: T): ScreenState<T>
	data class Error(val error: UiText): ScreenState<Nothing>
}