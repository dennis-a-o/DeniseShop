package com.example.deniseshop.domain.usercase.profile

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiUser
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetApiUserUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetApiUserUseCase {
	override fun invoke(): Flow<NetworkResponseState<ApiUser>> = apiRepository.getUser()
}