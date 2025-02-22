package com.example.deniseshop.domain.usercase.product

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiProductDetail
import kotlinx.coroutines.flow.Flow

interface GetProductDetailUseCase {
	operator fun invoke(id: Long): Flow<NetworkResponseState<ApiProductDetail>>
}