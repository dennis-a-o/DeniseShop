package com.example.deniseshop.domain.usercase.address

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetAddressDefaultUseCaseImpl @Inject constructor(private val apiRepository: ApiRepository): SetAddressDefaultUseCase {
	override fun invoke(id: Long): Flow<NetworkResponseState<String>> {
		return apiRepository.setAddressDefault(id)
	}
}