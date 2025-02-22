package com.example.deniseshop.domain.usercase.profile

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface ChangePasswordUseCase {
	operator fun invoke(currentPassword: String, newPassword: String): Flow<NetworkResponseState<String>>
}