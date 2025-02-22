package com.example.deniseshop.domain.usercase.brand

import androidx.paging.PagingSource
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.ui.models.ProductQueryParams
import com.example.deniseshop.ui.models.UiProduct
import javax.inject.Inject

class GetBrandProductUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetBrandProductUseCase {
	override fun invoke(brand: Long,queryParams: ProductQueryParams): PagingSource<Int, UiProduct> {
		return apiRepository.getBrandProduct(brand,queryParams)
	}
}