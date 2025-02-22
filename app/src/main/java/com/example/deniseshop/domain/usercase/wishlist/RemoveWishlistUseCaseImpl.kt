package com.example.deniseshop.domain.usercase.wishlist

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoveWishlistUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
):RemoveWishlistUseCase {
	override fun invoke(id: Long): Flow<NetworkResponseState<String>> {
		return  apiRepository.removeWishlist(id)
	}
}