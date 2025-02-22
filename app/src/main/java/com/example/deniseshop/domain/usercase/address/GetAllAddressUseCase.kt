package com.example.deniseshop.domain.usercase.address

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiAddress
import kotlinx.coroutines.flow.Flow

interface GetAllAddressUseCase {
	operator fun invoke() : Flow<NetworkResponseState<List<ApiAddress>>>
}