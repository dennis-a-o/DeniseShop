package com.example.deniseshop.domain.usercase.home

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiHome
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetHomeUseCase {
	override fun invoke(): Flow<NetworkResponseState<ApiHome>> {
		return apiRepository.getHome()
	}
}