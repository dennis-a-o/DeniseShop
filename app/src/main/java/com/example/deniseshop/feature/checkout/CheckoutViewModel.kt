package com.example.deniseshop.feature.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.PaymentMethod
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.core.presentation.toUiText
import com.example.deniseshop.navigation.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = CheckoutViewModel.Factory::class)
class CheckoutViewModel @AssistedInject constructor(
	private val shopRepository: ShopRepository,
	@Assisted val navKey: Route.Checkout
): ViewModel() {
	private val _token = navKey.token
	private val _payerId = navKey.payerId
	private val _status =  navKey.status

	private val _state = MutableStateFlow(CheckoutState())

	val state = _state.asStateFlow()

	init {
		getCheckout()

		if (_status == "success"){
			if(_token.isNotEmpty() && _payerId.isNotEmpty()) {
				checkoutPaypalPayment(
					token = _token,
					payerId = _payerId
				)
			}
		}else if(_status == "cancel"){
			cancelPaypalPayment()
		}
	}

	fun retry(){
		getCheckout()
	}

	fun onSelectPaymentMethod(paymentMethod: PaymentMethod){
		_state.update {
			it.copy(paymentMethod = paymentMethod)
		}
	}

	fun clearState(){
		_state.update {
			it.copy(
				isLoading = false,
				loadingError = null,
				isCheckingOut = false,
				checkoutError = null,
				checkoutCancel = null,
				checkoutSuccess = null,
				paypalPaymentUrl = null
			)
		}
	}

	fun getPaypalPaymentUrl(){
		viewModelScope.launch {
			_state.update {
				it.copy(isCheckingOut = true)
			}

			shopRepository.createPaypalPayment()
				.onSuccess { url ->
					_state.update {
						it.copy(
							isCheckingOut = false,
							paypalPaymentUrl = url
						)
					}
				}
				.onError { error ->
					_state.update {
						it.copy(
							isCheckingOut = false,
							checkoutError = error.toUiText()
						)
					}
				}
		}
	}

	fun placeOrder(){
		viewModelScope.launch {
			_state.update {
				it.copy(isCheckingOut = true)
			}

			shopRepository.placeOrder()
				.onSuccess { message ->
					_state.update {
						it.copy(
							isCheckingOut = false,
							checkoutSuccess = message
						)
					}
				}
				.onError { error ->
					_state.update {
						it.copy(
							isCheckingOut = false,
							checkoutError = error.toUiText()
						)
					}
				}
		}
	}

	private fun cancelPaypalPayment(){
		viewModelScope.launch {
			_state.update {
				it.copy(isCheckingOut = true)
			}

			shopRepository.paypalPaymentCancel()
				.onSuccess { message ->
					_state.update {
						it.copy(
							isCheckingOut = false,
							checkoutCancel = message
						)
					}
				}
				.onError { error ->
					_state.update {
						it.copy(
							isCheckingOut = false,
							checkoutError = error.toUiText()
						)
					}
				}
		}
	}

	private fun checkoutPaypalPayment(
		token: String,
		payerId: String,
	){
		viewModelScope.launch {
			_state.update {
				it.copy(isCheckingOut = true)
			}

			shopRepository.paypalPaymentSuccess(
				token = token,
				payerId = payerId
			)
				.onSuccess { message ->
					_state.update {
						it.copy(
							isCheckingOut = false,
							checkoutSuccess = message
						)
					}
				}
				.onError { error ->
					_state.update {
						it.copy(
							isCheckingOut = false,
							checkoutError = error.toUiText()
						)
					}
				}
		}
	}

	private fun getCheckout(){
		viewModelScope.launch {
			_state.update {
				it.copy(isLoading = true)
			}

			shopRepository.getCheckout()
				.onSuccess { checkout ->
					_state.update {
						it.copy(
							isLoading = false,
							checkout = checkout
						)
					}
				}
				.onError { error ->
					_state.update {
						it.copy(
							isLoading = false,
							loadingError = error.toUiText()
						)
					}
				}
		}
	}

	@AssistedFactory
	interface Factory {
		fun create(navKey: Route.Checkout): CheckoutViewModel
	}
}