package com.example.deniseshop.domain.usercase.auth

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.domain.models.UserSignInData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): SignInUseCase {
	override fun invoke(email:String, password:String): Flow<NetworkResponseState<String>> {
		return apiRepository.signIn(email, password)
	}
}