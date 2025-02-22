package com.example.deniseshop.domain.usercase.order

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiOrderDetail
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrderDetailUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
):GetOrderDetailUseCase {
	override fun invoke(id: Long): Flow<NetworkResponseState<ApiOrderDetail>> {
		return apiRepository.getOrderDetail(id)
	}
}