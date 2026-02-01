package com.example.deniseshop.core.domain.model

data class Slider(
	val id : Long,
	val title: String,
	val subTitle: String,
	val image: String,
	val highlightText: String,
	val description: String,
	val link: String,
	val type: String,
	val typeId: Long,
	val buttonText: String
)
