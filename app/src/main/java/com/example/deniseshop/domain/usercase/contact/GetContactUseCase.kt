package com.example.deniseshop.domain.usercase.contact

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiContact
import kotlinx.coroutines.flow.Flow

interface GetContactUseCase {
	operator fun invoke(): Flow<NetworkResponseState<List<ApiContact>>>
}