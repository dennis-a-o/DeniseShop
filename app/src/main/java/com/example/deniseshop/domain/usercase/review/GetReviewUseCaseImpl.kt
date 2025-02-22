package com.example.deniseshop.domain.usercase.review

import androidx.paging.PagingSource
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.ui.models.UiReview
import javax.inject.Inject

class GetReviewUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
):GetReviewUseCase {
	override fun invoke(product: Long): PagingSource<Int, UiReview> {
		return apiRepository.getReviews(product)
	}
}