package com.example.deniseshop.feature.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.deniseshop.core.domain.model.ReviewStat
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.navigation.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ReviewsViewModel.Factory::class)
class ReviewsViewModel @AssistedInject constructor(
	private val shopRepository: ShopRepository,
	@Assisted val navKey: Route.Reviews
): ViewModel() {
	private val productId = navKey.productId
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

	@AssistedFactory
	interface Factory {
		fun create(navKey: Route.Reviews): ReviewsViewModel
	}
}