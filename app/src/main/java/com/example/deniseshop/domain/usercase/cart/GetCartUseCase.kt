package com.example.deniseshop.domain.usercase.cart

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiCart
import kotlinx.coroutines.flow.Flow

interface GetCartUseCase {
	operator fun invoke(): Flow<NetworkResponseState<ApiCart>>
}