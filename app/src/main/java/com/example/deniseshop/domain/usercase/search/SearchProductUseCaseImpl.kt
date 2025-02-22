package com.example.deniseshop.domain.usercase.search

import androidx.paging.PagingSource
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.ui.models.ProductQueryParams
import com.example.deniseshop.ui.models.UiProduct
import javax.inject.Inject

class SearchProductUseCaseImpl @Inject  constructor(
	private val apiRepository: ApiRepository
): SearchProductUseCase {
	override fun invoke(queryParams: ProductQueryParams): PagingSource<Int, UiProduct> {
		return apiRepository.getProducts(queryParams)
	}
}