package com.example.deniseshop.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.core.domain.repository.UserSettingRepository
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
class CartViewModel @Inject constructor(
	private val shopRepository: ShopRepository,
	private val settingRepository: UserSettingRepository
): ViewModel() {
	private val _state = MutableStateFlow(CartState())

	val state = _state.asStateFlow()

	val wishlistItems = settingRepository.getWishlistItems()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList(),
		)

	init {
		getCart()
	}

	fun onEvent(event: CartEvent){
		when(event) {
			is CartEvent.ApplyCoupon -> applyCoupon(event.coupon)
			CartEvent.ClearCart -> clearCart()
			CartEvent.ClearCartState -> {
				_state.update {
					it.copy(
						error = null,
						success = null
					)
				}
			}
			CartEvent.ClearCoupon -> clearCoupon()
			is CartEvent.DecreaseQuantity -> decreaseQuantity(event.itemId)
			is CartEvent.IncreaseQuantity -> increaseQuantity(event.itemId)
			is CartEvent.RemoveFromCart -> removeFromCart(event.itemId)
			CartEvent.Refresh -> refresh()
		}
	}

	private fun refresh(){
		_state.update { it.copy(isLoading = true) }
		getCart()
	}

	private fun removeFromCart(itemId: Long){
		viewModelScope.launch {
			shopRepository.removeFromCart(itemId)
				.onSuccess {
					getCart()
				}
				.onError { error ->
					_state.update {
						it.copy(
							error = error.toUiText()
						)
					}
				}
		}
	}

	private fun increaseQuantity(itemId: Long){
		viewModelScope.launch {
			shopRepository.increaseCartItemQuantity(itemId)
				.onSuccess {
					getCart()
				}
				.onError { error ->
					_state.update {
						it.copy(
							error = error.toUiText()
						)
					}
				}
		}
	}

	private fun decreaseQuantity(itemId: Long){
		viewModelScope.launch {
			shopRepository.decreaseCartItemQuantity(itemId)
				.onSuccess {
					getCart()
				}
				.onError { error ->
					_state.update {
						it.copy(
							error = error.toUiText()
						)
					}
				}
		}
	}

	private fun clearCoupon(){
		viewModelScope.launch {
			shopRepository.clearCoupon()
				.onSuccess{
					getCart()
				}
				.onError { error ->
					_state.update {
						it.copy(
							isLoading = false,
							error = error.toUiText()
						)
					}
				}
		}
	}

	private fun clearCart(){
		viewModelScope.launch {
			_state.update { it.copy(isLoading = true) }

			shopRepository.clearCart()
				.onSuccess {
					_state.update {
						it.copy(
							isLoading = false,
							cart = null
						)
					}
				}
				.onError { error ->
					_state.update {
						it.copy(
							isLoading = false,
							error = error.toUiText()
						)
					}
				}
		}
	}

	private fun applyCoupon(coupon: String){
		viewModelScope.launch {
			_state.update { it.copy(isCouponLoading = true) }

			shopRepository.applyCoupon(coupon)
				.onSuccess { msg ->
					_state.update {
						it.copy(
							isCouponLoading = false,
							success = msg
						)
					}
				}
				.onError {error ->
					_state.update {
						it.copy(
							isCouponLoading = false,
							error = error.toUiText()
						)
					}
				}
		}
	}

	private fun getCart(){
		viewModelScope.launch {
			shopRepository.getCart()
				.onSuccess { cart ->
					_state.update {
						it.copy(
							isLoading = false,
							cart = cart
						)
					}
				}.onError { error ->
					_state.update {
						it.copy(
							isLoading = false,
							error = error.toUiText()
						)
					}
				}
		}
	}
}