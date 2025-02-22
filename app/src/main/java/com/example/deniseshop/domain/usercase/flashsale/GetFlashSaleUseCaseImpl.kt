package com.example.deniseshop.domain.usercase.flashsale

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiFlashSale
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFlashSaleUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
):GetFlashSaleUseCase {
	override fun invoke(id: Long): Flow<NetworkResponseState<ApiFlashSale>> {
		return apiRepository.getFlashSale(id)
	}
}