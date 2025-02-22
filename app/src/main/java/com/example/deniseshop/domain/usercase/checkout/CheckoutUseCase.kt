package com.example.deniseshop.domain.usercase.checkout

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiCheckout
import kotlinx.coroutines.flow.Flow

interface CheckoutUseCase {
	operator fun invoke(): Flow<NetworkResponseState<ApiCheckout>>
}