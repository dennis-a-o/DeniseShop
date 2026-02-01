package com.example.deniseshop.feature.cart

sealed interface CartEvent {
	data class IncreaseQuantity(val itemId: Long): CartEvent
	data class DecreaseQuantity(val itemId: Long): CartEvent
	data class RemoveFromCart(val itemId: Long): CartEvent
	data class ApplyCoupon(val coupon: String): CartEvent
	data object Refresh: CartEvent
	data object ClearCoupon: CartEvent
	data object ClearCart: CartEvent
	data object ClearCartState: CartEvent
}