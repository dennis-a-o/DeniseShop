package com.example.deniseshop.domain.usercase.auth

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface SignInUseCase {
	operator fun invoke(email:String, password: String): Flow<NetworkResponseState<String>>
}