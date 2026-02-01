package com.example.deniseshop.core.domain.model

data class Review(
	 val id: Long,
	 val reviewerName: String,
	 val reviewerImage: String,
	 val star: Int,
	 val comment: String,
	 val createdAt: String
)