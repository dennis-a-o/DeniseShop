package com.example.deniseshop.domain.usercase.home

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiHome
import kotlinx.coroutines.flow.Flow

interface GetHomeUseCase {
	operator fun invoke(): Flow<NetworkResponseState<ApiHome>>
}