package com.example.deniseshop.ui.screens.rencentViewed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.domain.usercase.recentViewed.ClearRecentViewedProductUseCase
import com.example.deniseshop.domain.usercase.recentViewed.GetRecentViewedProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecentViewedModel @Inject constructor(
	private val getRecentViewedProductUseCase: GetRecentViewedProductUseCase,
	private val clearRecentViewedProductUseCase: ClearRecentViewedProductUseCase
): ViewModel() {
	private val _isLoading = MutableStateFlow(false)
	private val _isError = MutableStateFlow(false)
	private val _isSuccess = MutableStateFlow(false)
	private val _message =MutableStateFlow("")

	val isLoading = _isLoading
	val isError = _isError
	val isSuccess = _isSuccess
	val message = _message

	val pager = Pager(PagingConfig(pageSize = 20,initialLoadSize = 20)){
		getRecentViewedProductUseCase()
	}.flow.cachedIn(viewModelScope)


	fun onClear(){
		clearRecentViewed()
	}

	fun successReset(){
		_isSuccess.value = false
		_message.value = ""
	}

	fun errorReset(){
		_isError.value = false
		_message.value = ""
	}

	private fun clearRecentViewed(){
		clearRecentViewedProductUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_isLoading.value = false
					_isError.value = true
					_message.value = it.exception.message.toString()
				}
				is NetworkResponseState.Loading -> {
					_isLoading.value = true
				}
				is NetworkResponseState.Success -> {
					_isLoading.value = false
					_isSuccess.value = true
					_message.value = it.result
				}
			}
		}.launchIn(viewModelScope)
	}
}