package com.example.deniseshop.ui.screens.order.viewModels

sealed class ReviewFormEvent {
	class StarChanged(val rating: Int): ReviewFormEvent()
	class ReviewChanged(val review: String): ReviewFormEvent()
	class Open(val productId: Long): ReviewFormEvent()
	data object ResetMessage: ReviewFormEvent()
	data object Close: ReviewFormEvent()
	data object Submit: ReviewFormEvent()
}