package com.example.deniseshop.domain.usercase

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiProductFilter
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductFilterUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetProductFilterUseCase {
	override fun invoke(category:Long, brand:Long): Flow<NetworkResponseState<ApiProductFilter>> {
		return apiRepository.getProductFilter(category, brand)
	}
}