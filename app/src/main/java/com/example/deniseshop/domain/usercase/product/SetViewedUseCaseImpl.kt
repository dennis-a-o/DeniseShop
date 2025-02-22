package com.example.deniseshop.domain.usercase.product

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetViewedUseCaseImpl @Inject constructor(
	private  val apiRepository: ApiRepository
): SetViewedUseCase {
	override fun invoke(productId: Long): Flow<NetworkResponseState<String>> {
		return apiRepository.setViewed(productId)
	}
}