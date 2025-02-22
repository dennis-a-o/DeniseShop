package com.example.deniseshop.domain.usercase.profile

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface DeleteAccountUseCase {
	operator fun invoke(): Flow<NetworkResponseState<String>>
}