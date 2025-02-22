package com.example.deniseshop.domain.usercase.contact

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiContact
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContactUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
):GetContactUseCase {
	override fun invoke(): Flow<NetworkResponseState<List<ApiContact>>> {
		return apiRepository.getContact()
	}
}