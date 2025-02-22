package com.example.deniseshop.domain.usercase.address

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiAddress
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddAddressUseCaseImpl @Inject constructor(private val apiRepository: ApiRepository): AddAddressUseCase {
	override fun invoke(address: ApiAddress): Flow<NetworkResponseState<String>> {
		return apiRepository.addAddress(address)
	}
}