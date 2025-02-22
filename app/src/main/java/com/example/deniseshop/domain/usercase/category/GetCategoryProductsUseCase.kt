package com.example.deniseshop.domain.usercase.category

import androidx.paging.PagingSource
import com.example.deniseshop.ui.models.ProductQueryParams
import com.example.deniseshop.ui.models.UiProduct

interface GetCategoryProductsUseCase {
	operator fun invoke(category: Long,queryParams: ProductQueryParams): PagingSource<Int, UiProduct>
}