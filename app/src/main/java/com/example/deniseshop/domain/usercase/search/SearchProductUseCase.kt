package com.example.deniseshop.domain.usercase.search

import androidx.paging.PagingSource
import com.example.deniseshop.ui.models.ProductQueryParams
import com.example.deniseshop.ui.models.UiProduct

interface SearchProductUseCase {
	operator fun invoke(queryParams: ProductQueryParams):PagingSource<Int, UiProduct>
}