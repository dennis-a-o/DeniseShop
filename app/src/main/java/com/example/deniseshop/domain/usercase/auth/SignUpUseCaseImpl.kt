package com.example.deniseshop.domain.usercase.auth

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.domain.models.UserSignUpData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): SignUpUseCase {
	override fun invoke(userSignUpData: UserSignUpData): Flow<NetworkResponseState<String>> {
		return apiRepository.signUp(userSignUpData)
	}
}