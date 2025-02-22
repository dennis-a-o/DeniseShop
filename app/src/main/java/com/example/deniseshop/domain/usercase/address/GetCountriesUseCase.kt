package com.example.deniseshop.domain.usercase.address

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface GetCountriesUseCase {
	operator fun invoke(): Flow<NetworkResponseState<List<String>>>
}