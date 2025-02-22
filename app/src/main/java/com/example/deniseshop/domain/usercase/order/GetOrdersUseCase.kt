package com.example.deniseshop.domain.usercase.order

import androidx.paging.PagingSource
import com.example.deniseshop.ui.models.UiOrder

interface GetOrdersUseCase {
	operator fun invoke():PagingSource<Int,UiOrder>
}