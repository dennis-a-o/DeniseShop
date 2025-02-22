package com.example.deniseshop.domain.usercase.order

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddReviewUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): AddReviewUseCase {
	override fun invoke(productId: Long, rating: Int, review: String): Flow<NetworkResponseState<String>> {
		return apiRepository.addReview(productId, rating, review)
	}
}