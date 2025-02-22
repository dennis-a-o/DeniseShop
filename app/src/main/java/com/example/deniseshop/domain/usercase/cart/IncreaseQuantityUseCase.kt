package com.example.deniseshop.domain.usercase.cart

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface IncreaseQuantityUseCase {
	operator fun invoke(id: Long): Flow<NetworkResponseState<String>>
}