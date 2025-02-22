package com.example.deniseshop.domain.usercase.order

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface AddReviewUseCase {
	operator fun invoke(productId: Long, rating:Int, review: String): Flow<NetworkResponseState<String>>
}