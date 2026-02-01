package com.example.deniseshop.core.domain.model

data class Page(
	val id: Long,
	val name: String,
	val image: String?,
	val description: String?,
	val content: String
)