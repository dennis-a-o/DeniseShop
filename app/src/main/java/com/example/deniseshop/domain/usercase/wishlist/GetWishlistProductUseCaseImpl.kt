package com.example.deniseshop.domain.usercase.wishlist

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWishlistProductUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetWishlistProductUseCase {
	override fun invoke(): Flow<NetworkResponseState<List<Long>>> {
		return apiRepository.getWishlistProduct()
	}
}