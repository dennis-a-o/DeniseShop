package com.example.deniseshop.domain.usercase.recentViewed

import androidx.paging.PagingSource
import com.example.deniseshop.ui.models.UiProduct

interface GetRecentViewedProductUseCase {
	operator fun invoke(): PagingSource<Int, UiProduct>
}