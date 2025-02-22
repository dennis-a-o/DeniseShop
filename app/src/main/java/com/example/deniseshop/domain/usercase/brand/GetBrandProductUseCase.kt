package com.example.deniseshop.domain.usercase.brand

import androidx.paging.PagingSource
import com.example.deniseshop.ui.models.ProductQueryParams
import com.example.deniseshop.ui.models.UiProduct

interface GetBrandProductUseCase {
	operator fun invoke(brand: Long,queryParams: ProductQueryParams): PagingSource<Int, UiProduct>
}