package com.example.deniseshop.ui.models

data class ReviewFormState(
	val productId: Long = 0,
	val rating: Int = 0,
	val ratingError: String? = null,
	val review: String = "",
	val reviewError: String? = null,
	val isLoading: Boolean = false,
	val isError: Boolean = false,
	val isSuccess: Boolean = false,
	val message:String = ""
)
