package com.example.deniseshop.domain.usercase.recentViewed

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface ClearRecentViewedProductUseCase {
	operator fun  invoke(): Flow<NetworkResponseState<String>>
}