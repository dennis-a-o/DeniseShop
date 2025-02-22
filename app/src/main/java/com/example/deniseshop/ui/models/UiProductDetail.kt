package com.example.deniseshop.ui.models

data class UiProductDetail(
	val product: UiProduct,
	val reviewStat: UiReviewStat,
	val reviews: List<UiReview>
)
