package com.example.deniseshop.domain.usercase.flashsale

import androidx.paging.PagingSource
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.ui.models.ProductQueryParams
import com.example.deniseshop.ui.models.UiProduct
import javax.inject.Inject

class GetFlashSaleProductUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
):GetFlashSaleProductUseCase {
	override fun invoke(flashSale: Long, queryParams: ProductQueryParams): PagingSource<Int, UiProduct> {
		return apiRepository.getFlashSaleProduct(flashSale, queryParams)
	}
}