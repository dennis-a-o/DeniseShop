package com.example.deniseshop.domain.usercase.wishlist

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddWishlistUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): AddWishlistUseCase {
	override fun invoke(product: Long): Flow<NetworkResponseState<String>> {
		return apiRepository.addWishlist(product)
	}
}