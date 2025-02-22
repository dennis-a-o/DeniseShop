package com.example.deniseshop.domain.usercase.brand

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiBrand
import kotlinx.coroutines.flow.Flow

interface GetCategoryBrandsUseCase {
	operator fun invoke(category: Long): Flow<NetworkResponseState<List<ApiBrand>>>
}