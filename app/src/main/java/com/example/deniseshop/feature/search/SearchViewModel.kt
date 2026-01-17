package com.example.deniseshop.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.deniseshop.core.domain.model.ProductData
import com.example.deniseshop.core.domain.model.ProductFilterParams
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.core.domain.repository.UserSettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val shopRepository: ShopRepository,
	private val settingRepository: UserSettingRepository
): ViewModel() {
	private val _query = MutableStateFlow("")

	val query = _query.asStateFlow()

	val productsPagingSource = Pager(
		config = PagingConfig(pageSize = 20,initialLoadSize = 20),
		pagingSourceFactory = {
			shopRepository.getProducts(
				filterParams = ProductFilterParams(query = _query.value)
			)
		}
	).flow.cachedIn(viewModelScope)

	val queryHistory = settingRepository.getResentSearchQueries()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = listOf()
		)

	val cartItems = settingRepository.getCartItems()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList(),
		)

	val wishlistItems = settingRepository.getWishlistItems()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList(),
		)

	fun onQueryChange(query: String){
		_query.value = query
	}

	fun clearQuery(query: String){
		viewModelScope.launch {
			settingRepository.removeSearchQuery(query)
		}
	}

	fun onCartToggle(productId: Long){
		viewModelScope.launch {
			if (productId in cartItems.value){
				shopRepository.removeFromCart(productId)
			}else{
				shopRepository.addToCart(
					ProductData(
						productId = productId
					)
				)
			}
		}
	}

	fun onWishlistToggle(productId: Long){
		viewModelScope.launch {
			if (productId in wishlistItems.value){
				shopRepository.removeFromWishlist(productId)
			}else{
				shopRepository.addToWishlist(productId)
			}
		}
	}
}