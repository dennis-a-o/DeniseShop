package com.example.deniseshop.domain.usercase.cart

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface ApplyCouponUseCase {
	operator fun invoke(coupon: String): Flow<NetworkResponseState<String>>
}