package com.example.deniseshop.domain.usercase.profile

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteAccountUseCaseImpl @Inject constructor(
	private val  apiRepository: ApiRepository
): DeleteAccountUseCase {
	override fun invoke(): Flow<NetworkResponseState<String>> = apiRepository.deleteAccount()
}