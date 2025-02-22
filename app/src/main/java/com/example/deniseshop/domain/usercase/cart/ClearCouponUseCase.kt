package com.example.deniseshop.domain.usercase.cart

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface ClearCouponUseCase {
	operator fun invoke(): Flow<NetworkResponseState<String>>
}