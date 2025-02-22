package com.example.deniseshop.domain.usercase.auth

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.domain.models.UserSignUpData
import kotlinx.coroutines.flow.Flow

interface SignUpUseCase {
	operator fun invoke(userSignUpData: UserSignUpData): Flow<NetworkResponseState<String>>
}