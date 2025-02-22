package com.example.deniseshop.domain.usercase.category

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiCategory
import kotlinx.coroutines.flow.Flow

interface GetCategoryUseCase {
	operator fun  invoke(): Flow<NetworkResponseState<List<ApiCategory>>>
	operator fun invoke(id: Long): Flow<NetworkResponseState<ApiCategory>>
}