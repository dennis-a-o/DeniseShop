package com.example.deniseshop.domain.usercase.review

import androidx.paging.PagingSource
import com.example.deniseshop.ui.models.UiReview

interface GetReviewUseCase {
	operator fun invoke(product: Long): PagingSource<Int, UiReview>
}