package com.example.deniseshop.domain.usercase.wishlist

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface GetWishlistProductUseCase {
	operator fun invoke(): Flow<NetworkResponseState<List<Long>>>
}