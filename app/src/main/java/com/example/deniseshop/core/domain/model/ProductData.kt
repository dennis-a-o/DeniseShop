package com.example.deniseshop.core.domain.model

data class ProductData(
	val productId: Long,
	val quantity: Int? = null,
	val size: String? = null,
	val color:String? = null
)
