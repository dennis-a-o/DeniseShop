package com.example.deniseshop.feature.checkout

import com.example.deniseshop.core.domain.model.Checkout
import com.example.deniseshop.core.domain.model.PaymentMethod
import com.example.deniseshop.core.presentation.UiText

data class CheckoutState(
	val checkout: Checkout? = null,
	val paymentMethod: PaymentMethod? =null,
	val paypalPaymentUrl: String? = null,
	val isLoading: Boolean = false,
	val loadingError: UiText? = null,
	val isCheckingOut: Boolean = false,
	val checkoutError: UiText? = null,
	val checkoutSuccess: String? = null,
	val checkoutCancel: String? = null
)
