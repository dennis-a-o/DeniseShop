package com.example.deniseshop.domain.usercase.product

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiProductDetail
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductDetailUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetProductDetailUseCase {
	override fun invoke(id: Long): Flow<NetworkResponseState<ApiProductDetail>> {
		return apiRepository.getProductDetail(id)
	}
}