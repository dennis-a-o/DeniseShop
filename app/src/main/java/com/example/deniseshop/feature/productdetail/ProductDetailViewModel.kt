package com.example.deniseshop.feature.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.ProductData
import com.example.deniseshop.core.domain.model.ProductDetail
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.core.domain.repository.UserSettingRepository
import com.example.deniseshop.core.presentation.ScreenState
import com.example.deniseshop.core.presentation.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
	private val shopRepository: ShopRepository,
	private val settingRepository: UserSettingRepository,
	savedStateHandle: SavedStateHandle,
): ViewModel() {
	private val productId = savedStateHandle["productId"] ?: 0L

	private val _state = MutableStateFlow<ScreenState<ProductDetail>>(ScreenState.Loading)
	private val _specState = MutableStateFlow(ProductDetailSpecState())

	val state = _state.asStateFlow()
	val specState = _specState.asStateFlow()

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

	init {
		getProductDetail()
	}

	fun onEvent(event: ProductDetailEvent){
		when(event) {
			is ProductDetailEvent.DecreaseQuantity -> {
				_specState.update {
					it.copy(
						quantity = if(event.quantity > 1){
								event.quantity -1
							} else 1

					)
				}
			}
			is ProductDetailEvent.IncreaseQuantity -> {
				_specState.update {
					it.copy(quantity = event.quantity + 1)
				}
			}
			ProductDetailEvent.Refresh -> {
				getProductDetail(isRefresh = true)
			}
			is ProductDetailEvent.SelectColor -> {
				_specState.update {
					it.copy(color = event.color)
				}
			}
			is ProductDetailEvent.SelectSize -> {
				_specState.update {
					it.copy(size= event.size)
				}
			}
			ProductDetailEvent.ToggleCart -> onCartToggle()
			ProductDetailEvent.ToggleWishlist -> onWishlistToggle()
			ProductDetailEvent.Retry -> getProductDetail()
		}
	}

	private fun onCartToggle(){
		viewModelScope.launch {
			if (productId in cartItems.value){
				shopRepository.removeFromCart(productId)
			}else{
				shopRepository.addToCart(
					ProductData(
						productId = productId,
						quantity = _specState.value.quantity,
						size = _specState.value.size,
						color = _specState.value.color
					)
				)
			}
		}
	}

	private fun onWishlistToggle(){
		viewModelScope.launch {
			if (productId in wishlistItems.value){
				shopRepository.removeFromWishlist(productId)
			}else{
				shopRepository.addToWishlist(productId)
			}
		}
	}


	private  fun getProductDetail(isRefresh: Boolean = false){
		viewModelScope.launch {

			if (!isRefresh){
				_state.value = ScreenState.Loading
			}
			shopRepository.getProductDetail(productId)
				.onSuccess { productDetail ->
					_state.value = ScreenState.Success(productDetail)

					if (!isRefresh) {
						_specState.update {
							it.copy(
								color = productDetail.product.color?.firstOrNull()?.split("=")[0],
								size = productDetail.product.size?.firstOrNull()
							)
						}
					}

					setProductViewed()
				}
				.onError {
					_state.value = ScreenState.Error(it.toUiText())
				}
		}
	}

	private fun setProductViewed(){
		viewModelScope.launch {
			shopRepository.setProductViewed(productId)
				.onSuccess {  }
				.onError {  }
		}
	}
}