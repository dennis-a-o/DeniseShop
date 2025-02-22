package com.example.deniseshop.domain.usercase.cart

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface RemoveCartUseCase {
	operator fun invoke(product: Long): Flow<NetworkResponseState<String>>
}