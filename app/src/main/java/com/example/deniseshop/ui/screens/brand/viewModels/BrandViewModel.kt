package com.example.deniseshop.ui.screens.brand.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.deniseshop.domain.usercase.brand.GetBrandUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BrandViewModel @Inject constructor(
	private val getBrandUseCase: GetBrandUseCase,
): ViewModel() {
	val pager = Pager(PagingConfig(pageSize = 20, initialLoadSize = 20)){
		getBrandUseCase()
	}.flow.cachedIn(viewModelScope)
}