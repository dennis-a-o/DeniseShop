package com.example.deniseshop.domain.usercase.address

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiAddress
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAddressUseCaseImpl @Inject constructor(private val apiRepository: ApiRepository): GetAllAddressUseCase {
	override fun invoke(): Flow<NetworkResponseState<List<ApiAddress>>> {
		return apiRepository.getAllAddress()
	}
}