package com.example.deniseshop.feature.productdetail

sealed interface ProductDetailEvent {
	data class IncreaseQuantity(val quantity: Int): ProductDetailEvent
	data class DecreaseQuantity(val quantity: Int): ProductDetailEvent
	data class SelectColor(val color: String): ProductDetailEvent
	data class SelectSize(val size: String): ProductDetailEvent
	data object ToggleCart: ProductDetailEvent
	data object ToggleWishlist: ProductDetailEvent
	data object Refresh: ProductDetailEvent
	data object Retry: ProductDetailEvent
}