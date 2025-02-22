package com.example.deniseshop.domain.usercase.checkout

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface CreatePaypalPaymentUseCase {
	operator fun invoke(): Flow<NetworkResponseState<String>>
}