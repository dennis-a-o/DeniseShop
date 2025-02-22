package com.example.deniseshop.domain.usercase.product

import androidx.paging.PagingSource
import com.example.deniseshop.ui.models.ProductQueryParams
import com.example.deniseshop.ui.models.UiProduct

interface GetProductUseCase {
	operator fun invoke(queryParams: ProductQueryParams): PagingSource<Int, UiProduct>
}