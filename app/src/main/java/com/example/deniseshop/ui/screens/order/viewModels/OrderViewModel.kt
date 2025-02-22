package com.example.deniseshop.ui.screens.order.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.deniseshop.domain.usercase.order.GetOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
	private val getOrdersUseCase: GetOrdersUseCase
): ViewModel() {
	val pager = Pager(PagingConfig(pageSize = 20, initialLoadSize = 20)){
		getOrdersUseCase()
	}.flow.cachedIn(viewModelScope)
}