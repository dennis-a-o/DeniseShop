package com.example.deniseshop.feature.coupons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.deniseshop.core.domain.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CouponsViewModel @Inject constructor(
	private val shopRepository: ShopRepository
): ViewModel() {
	val couponsPagingSource = Pager(
		config = PagingConfig(initialLoadSize = 20, pageSize = 20)
	){
		shopRepository.getCoupons()
	}.flow.cachedIn(viewModelScope)
}