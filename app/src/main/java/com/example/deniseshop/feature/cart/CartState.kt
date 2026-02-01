package com.example.deniseshop.feature.cart

import com.example.deniseshop.core.domain.model.Cart
import com.example.deniseshop.core.presentation.UiText

data class CartState(
	val cart: Cart? = null,
	val isLoading: Boolean = true,
	val isCouponLoading: Boolean = false,
	val error: UiText? = null,
	val success: String? = null
)
