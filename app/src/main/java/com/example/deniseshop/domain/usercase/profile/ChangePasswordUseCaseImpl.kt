package com.example.deniseshop.domain.usercase.profile

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangePasswordUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): ChangePasswordUseCase {
	override fun invoke(
		currentPassword: String,
		newPassword: String
	): Flow<NetworkResponseState<String>> {
		return apiRepository.changePassword(currentPassword, newPassword)
	}
}