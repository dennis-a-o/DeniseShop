package com.example.deniseshop.ui.models

data class UiReview(
	val id: Long,
	val reviewerName: String,
	val reviewerImage: String,
	val star: Int,
	val comment: String,
	val createdAt: String
)
