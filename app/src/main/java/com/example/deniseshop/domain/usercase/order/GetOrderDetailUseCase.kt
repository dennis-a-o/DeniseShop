package com.example.deniseshop.domain.usercase.order

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiOrderDetail
import kotlinx.coroutines.flow.Flow

interface GetOrderDetailUseCase {
	operator fun invoke(id: Long): Flow<NetworkResponseState<ApiOrderDetail>>
}