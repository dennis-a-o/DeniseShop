package com.example.deniseshop.core.domain.model

data class Category(
	val id: Long,
	val parentId: Long,
	val name: String,
	val image: String? = null,
	val icon: String? = null,
	val categories: List<Category>? = null,
	val brands: List<Brand>? = null
)
