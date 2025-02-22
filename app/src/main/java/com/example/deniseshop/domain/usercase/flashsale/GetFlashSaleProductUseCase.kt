package com.example.deniseshop.domain.usercase.flashsale

import androidx.paging.PagingSource
import com.example.deniseshop.ui.models.ProductQueryParams
import com.example.deniseshop.ui.models.UiProduct

interface GetFlashSaleProductUseCase {
	operator fun invoke(flashSale: Long, queryParams: ProductQueryParams): PagingSource<Int, UiProduct>
}