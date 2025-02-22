package com.example.deniseshop.domain.usercase.checkout

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface PlaceOrderUseCase {
	operator fun invoke(): Flow<NetworkResponseState<String>>
}