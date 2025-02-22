package com.example.deniseshop.domain.usercase.auth

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ForgotPasswordUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): ForgotPasswordUseCase {
	override fun invoke(email: String): Flow<NetworkResponseState<String>> {
		return  apiRepository.forgotPassword(email)
	}
}