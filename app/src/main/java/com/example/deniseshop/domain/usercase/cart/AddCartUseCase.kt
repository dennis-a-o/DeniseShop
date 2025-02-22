package com.example.deniseshop.domain.usercase.cart

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface AddCartUseCase {
	operator fun invoke(product: Long,quantity: Int?, size: String?, color: String?):Flow<NetworkResponseState<String>>
}