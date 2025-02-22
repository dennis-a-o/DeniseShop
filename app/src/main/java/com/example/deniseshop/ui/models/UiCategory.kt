package com.example.deniseshop.ui.models

data class UiCategory(
	val id: Long,
	val parentId: Long,
	val name: String,
	val image: String?,
	val icon: String?,
	val categories: List<UiCategory>?,
	val brands: List<UiBrand>?
)