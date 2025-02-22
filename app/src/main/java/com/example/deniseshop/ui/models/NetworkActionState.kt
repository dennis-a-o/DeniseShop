package com.example.deniseshop.ui.models

data class NetworkActionState(
	val isLoading: Boolean = false,
	val isError: Boolean = false,
	val isSuccess: Boolean = false,
	val message: String = ""
)
