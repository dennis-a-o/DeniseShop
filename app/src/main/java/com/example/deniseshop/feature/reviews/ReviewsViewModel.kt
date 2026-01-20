package com.example.deniseshop.feature.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.deniseshop.core.domain.model.ReviewStat
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
	private val shopRepository: ShopRepository,
	savedStateHandle: SavedStateHandle,
): ViewModel() {
	private val productId = savedStateHandle["productId"] ?: 0L
	private val _reviewStat = MutableStateFlow<ReviewStat?>(null)

	val reviewsPagingSource = Pager(
		config = PagingConfig(initialLoadSize = 20, pageSize = 20),
		pagingSourceFactory = {
			shopRepository.getReviews(productId)
		}
	).flow.cachedIn(viewModelScope)

	val reviewStat = _reviewStat.asStateFlow()

	init {
		getReviewStat()
	}

	private fun getReviewStat() {
		viewModelScope.launch {
			shopRepository.getProductReviewStat(productId)
				.onSuccess {
					_reviewStat.value = it
				}
				.onError {  }
		}
	}
}