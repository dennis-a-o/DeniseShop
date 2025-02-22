package com.example.deniseshop.domain.usercase.wishlist

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow


interface RemoveWishlistUseCase {
	operator fun invoke(id: Long): Flow<NetworkResponseState<String>>
}