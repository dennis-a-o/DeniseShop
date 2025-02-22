package com.example.deniseshop.domain.usercase.review

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiReviewStat
import kotlinx.coroutines.flow.Flow

interface GetReviewStatUseCase {
	operator fun  invoke(product: Long): Flow<NetworkResponseState<ApiReviewStat>>
}