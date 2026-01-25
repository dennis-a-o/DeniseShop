package com.example.deniseshop.core.domain.model

data class OrderProduct(
	val id: Long,
	val productId: Long,
	val name: String,
	val image: String,
	val price: String,
	val quantity: Int,
	val size: String?,
	val color: String?,
	val rated: Boolean,
	val downloadable: Boolean
)