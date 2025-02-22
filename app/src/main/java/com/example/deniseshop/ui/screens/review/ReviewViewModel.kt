package com.example.deniseshop.ui.screens.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiReviewStat
import com.example.deniseshop.domain.usercase.review.GetReviewStatUseCase
import com.example.deniseshop.domain.usercase.review.GetReviewUseCase
import com.example.deniseshop.ui.mapper.BaseMapper
import com.example.deniseshop.ui.models.UiReviewStat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
	private val getReviewUseCase: GetReviewUseCase,
	private val getReviewStatUseCase: GetReviewStatUseCase,
	private val reviewStatAPiToUiMapper: BaseMapper<ApiReviewStat, UiReviewStat>,
	savedStateHandle: SavedStateHandle
): ViewModel() {
	private val productId: Long = savedStateHandle["productId"] ?: 0
	private val _reviewStat = MutableStateFlow<UiReviewStat?>(null)

	val reviewStat = _reviewStat.asStateFlow()

	val pager = Pager(PagingConfig(pageSize = 20,initialLoadSize = 20)){
		getReviewUseCase(productId)
	}.flow.cachedIn(viewModelScope)

	init{
		getReviewStat()
	}

	private fun getReviewStat(){
		getReviewStatUseCase(productId).onEach {
			when(it){
				is NetworkResponseState.Error -> {}
				is NetworkResponseState.Loading -> {}
				is NetworkResponseState.Success -> {
					_reviewStat.value = reviewStatAPiToUiMapper.map(it.result)
				}
			}
		}.launchIn(viewModelScope)
	}
}