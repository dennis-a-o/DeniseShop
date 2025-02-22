package com.example.deniseshop.domain.usercase.address

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiAddress
import kotlinx.coroutines.flow.Flow

interface AddAddressUseCase {
	operator fun invoke(address: ApiAddress): Flow<NetworkResponseState<String>>
}