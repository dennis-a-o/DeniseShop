package com.example.deniseshop.domain.usercase.product

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface SetViewedUseCase {
	operator fun invoke(productId: Long): Flow<NetworkResponseState<String>>
}