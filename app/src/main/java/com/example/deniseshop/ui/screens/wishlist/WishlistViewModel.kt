package com.example.deniseshop.ui.screens.wishlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.domain.usercase.GetLoggedInStateUseCase
import com.example.deniseshop.domain.usercase.wishlist.AddWishlistUseCase
import com.example.deniseshop.domain.usercase.wishlist.GetWishlistProductUseCase
import com.example.deniseshop.domain.usercase.wishlist.GetWishlistUseCase
import com.example.deniseshop.domain.usercase.wishlist.RemoveWishlistUseCase
import com.example.deniseshop.ui.models.NetworkActionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
	private val getWishlistProductUseCase: GetWishlistProductUseCase,
	private val getWishlistUseCase: GetWishlistUseCase,
	private val addWishlistUseCase: AddWishlistUseCase,
	private val removeWishlistUseCase: RemoveWishlistUseCase,
	private val getLoggedInStateUseCase: GetLoggedInStateUseCase
): ViewModel() {
	private val _pagerFlow = Pager(PagingConfig(pageSize = 20,initialLoadSize = 20)){ getWishlistUseCase() }
	private val _isRefreshing = MutableStateFlow(false)
	private val _isLoggedIn = MutableStateFlow(false)
	private val _wishlistCountState = MutableStateFlow(value = 0)
	private val _wishlistItems = MutableStateFlow(setOf<Long>())
	private val _responseState = MutableStateFlow(NetworkActionState())

	val wishlistCount = _wishlistCountState.asStateFlow()
	val isLoggedIn = _isLoggedIn.asStateFlow()
	val wishlistItems = _wishlistItems.asStateFlow()
	val responseState = _responseState.asStateFlow()
	val isRefreshing = _isRefreshing.asStateFlow()
	var pager = _pagerFlow.flow//.cachedIn(viewModelScope)

	init {
		checkIsLoggedIn()
		getWishlistItems()
	}

	fun resetResponseState(){
		_responseState.value = NetworkActionState()
	}

	fun onRefresh(){
		getWishlistItems()
	}

	fun onToggleWishlist(productId: Long){
		if (_wishlistItems.value.contains(productId)){
			removeWishlist(productId)
		}else{
			addToWishlist(productId)
		}
	}

	fun onRemoveWishlist(productId: Long){
		removeWishlist(productId)
	}

	private fun checkIsLoggedIn(){
		getLoggedInStateUseCase().onEach {
			_isLoggedIn.value = it
		}.launchIn(viewModelScope)
	}

	private fun getWishlistItems(){
		getWishlistProductUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					Log.e("WISHLIST_VM", it.exception.toString())
				}
				is NetworkResponseState.Loading -> {}
				is NetworkResponseState.Success -> {
					_isRefreshing.value = false
					_wishlistItems.value = it.result.toMutableSet()
					_wishlistCountState.value = it.result.size
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun addToWishlist(product:Long){
		addWishlistUseCase(product).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					Log.e("WISHLIST_VM", it.exception.toString())
					_responseState.value = _responseState.value.copy(
						isLoading = false,
						isError = false,
						isSuccess = true,
						message = it.exception.message.toString()
					)
				}
				is NetworkResponseState.Loading -> {
					_responseState.value = _responseState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					_wishlistItems.value = _wishlistItems.value.plus(product)

					_wishlistCountState.value += 1

					_responseState.value = _responseState.value.copy(
						isLoading = false,
						isError = false,
						isSuccess = true,
						message = it.result
					)

				}
			}
		}.launchIn(viewModelScope)
	}

	private fun removeWishlist(productId: Long){
		removeWishlistUseCase(productId).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					Log.e("WISHLIST_VM", it.exception.toString())
					_responseState.value = _responseState.value.copy(
						isLoading = false,
						isError = false,
						isSuccess = true,
						message = it.exception.message.toString()
					)
				}
				is NetworkResponseState.Loading -> {
					_responseState.value = _responseState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					_responseState.value = _responseState.value.copy(
						isLoading = false,
						isError = false,
						isSuccess = true,
						message = it.result
					)

					_wishlistItems.value = _wishlistItems.value.minus(productId)

					if (_wishlistCountState.value >= 1){
						_wishlistCountState.value -= 1
					}
				}
			}
		}.launchIn(viewModelScope)
	}

}