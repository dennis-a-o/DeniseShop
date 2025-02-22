package com.example.deniseshop.domain.usercase

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiProductFilter
import kotlinx.coroutines.flow.Flow

interface GetProductFilterUseCase {
	operator fun invoke(category:Long, brand:Long): Flow<NetworkResponseState<ApiProductFilter>>
}