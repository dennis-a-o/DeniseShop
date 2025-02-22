package com.example.deniseshop.domain.usercase.checkout

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface PaypalPaymentSuccessUseCase {
	operator fun invoke(token:String, payerId: String): Flow<NetworkResponseState<String>>
}