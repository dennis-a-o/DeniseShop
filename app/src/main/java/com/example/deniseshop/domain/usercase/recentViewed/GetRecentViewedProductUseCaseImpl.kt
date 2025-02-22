package com.example.deniseshop.domain.usercase.recentViewed

import androidx.paging.PagingSource
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.ui.models.UiProduct
import javax.inject.Inject

class GetRecentViewedProductUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetRecentViewedProductUseCase {
	override fun invoke(): PagingSource<Int, UiProduct> {
		return apiRepository.getRecentViewedProduct()
	}
}