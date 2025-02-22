package com.example.deniseshop.ui.screens.coupon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.deniseshop.domain.usercase.coupon.GetCouponUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CouponViewModel @Inject constructor(
    private val getCouponUseCase: GetCouponUseCase
): ViewModel() {
    val pager = Pager(PagingConfig(pageSize = 20, initialLoadSize = 20)){
        getCouponUseCase()
    }.flow.cachedIn(viewModelScope)
}