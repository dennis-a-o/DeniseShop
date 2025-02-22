package com.example.deniseshop.domain.usercase.flashsale

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiFlashSale
import kotlinx.coroutines.flow.Flow

interface GetFlashSaleUseCase {
	operator fun invoke(id: Long): Flow<NetworkResponseState<ApiFlashSale>>
}