package com.example.deniseshop.domain.usercase.recentViewed

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClearRecentViewedProductUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): ClearRecentViewedProductUseCase {
	override fun invoke(): Flow<NetworkResponseState<String>> {
		return  apiRepository.clearRecentViewedProduct()
	}
}