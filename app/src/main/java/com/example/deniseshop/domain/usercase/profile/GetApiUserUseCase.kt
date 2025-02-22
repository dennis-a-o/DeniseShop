package com.example.deniseshop.domain.usercase.profile

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiUser
import kotlinx.coroutines.flow.Flow

interface GetApiUserUseCase {
	operator fun invoke(): Flow<NetworkResponseState<ApiUser>>
}