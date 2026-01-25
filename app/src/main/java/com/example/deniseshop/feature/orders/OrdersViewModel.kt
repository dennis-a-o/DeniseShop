package com.example.deniseshop.feature.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.deniseshop.core.domain.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
	private val shopRepository: ShopRepository
): ViewModel() {
	val ordersPagingSource = Pager(
		config = PagingConfig(initialLoadSize = 20, pageSize = 20),
		pagingSourceFactory = {
			shopRepository.getOrders()
		}
	).flow.cachedIn(viewModelScope)
}