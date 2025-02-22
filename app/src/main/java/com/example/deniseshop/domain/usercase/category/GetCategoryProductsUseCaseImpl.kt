package com.example.deniseshop.domain.usercase.category

import androidx.paging.PagingSource
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.ui.models.ProductQueryParams
import com.example.deniseshop.ui.models.UiProduct
import javax.inject.Inject

class GetCategoryProductsUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetCategoryProductsUseCase {
	override fun invoke(category: Long,queryParams: ProductQueryParams): PagingSource<Int, UiProduct> {
		return apiRepository.getCategoryProduct(category,queryParams)
	}
}