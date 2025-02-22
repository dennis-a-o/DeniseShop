package com.example.deniseshop.domain.usercase.order

import androidx.paging.PagingSource
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.ui.models.UiOrder
import javax.inject.Inject

class GetOrdersUseCaseImpl @Inject constructor(
	private val  apiRepository: ApiRepository
):GetOrdersUseCase {
	override fun invoke(): PagingSource<Int, UiOrder> {
		return apiRepository.getOrders()
	}
}