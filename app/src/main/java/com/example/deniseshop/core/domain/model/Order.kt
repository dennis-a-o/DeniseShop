package com.example.deniseshop.core.domain.model

data class Order(
	val id: Long,
	val name: String,
	val image: String,
	val code: String,
	val status: String,
	val date: String
)