package com.example.deniseshop.domain.usercase.auth

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface ForgotPasswordUseCase {
	operator fun invoke(email: String): Flow<NetworkResponseState<String>>
}