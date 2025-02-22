package com.example.deniseshop.domain.usercase.review

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiReviewStat
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReviewStatUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetReviewStatUseCase {
	override fun invoke(product: Long): Flow<NetworkResponseState<ApiReviewStat>> {
		return apiRepository.getReviewStat(product)
	}
}