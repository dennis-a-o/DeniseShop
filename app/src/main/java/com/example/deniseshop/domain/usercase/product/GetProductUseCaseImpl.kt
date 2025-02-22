package com.example.deniseshop.domain.usercase.product

import androidx.paging.PagingSource
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.ui.models.ProductQueryParams
import com.example.deniseshop.ui.models.UiProduct
import javax.inject.Inject

class GetProductUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetProductUseCase {
	override fun invoke(queryParams: ProductQueryParams): PagingSource<Int, UiProduct> {
		return apiRepository.getProducts(queryParams)
	}
}