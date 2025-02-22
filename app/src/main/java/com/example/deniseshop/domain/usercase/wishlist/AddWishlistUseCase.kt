package com.example.deniseshop.domain.usercase.wishlist

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface AddWishlistUseCase {
	operator fun invoke(product: Long): Flow<NetworkResponseState<String>>
}