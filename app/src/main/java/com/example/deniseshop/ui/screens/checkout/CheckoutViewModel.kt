package com.example.deniseshop.ui.screens.checkout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.data.models.ApiCheckout
import com.example.deniseshop.domain.usercase.checkout.CheckoutUseCase
import com.example.deniseshop.domain.usercase.checkout.CreatePaypalPaymentUseCase
import com.example.deniseshop.domain.usercase.checkout.PaypalPaymentCancelUseCase
import com.example.deniseshop.domain.usercase.checkout.PaypalPaymentSuccessUseCase
import com.example.deniseshop.domain.usercase.checkout.PlaceOrderUseCase
import com.example.deniseshop.ui.mapper.BaseMapper
import com.example.deniseshop.ui.models.NetworkActionState
import com.example.deniseshop.ui.models.UiCheckout
import com.example.deniseshop.ui.models.UiPaymentMethod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
	private val checkoutUseCase: CheckoutUseCase,
	private val checkoutApiToUiMapper: BaseMapper<ApiCheckout,UiCheckout>,
	private val placeOrderUseCase: PlaceOrderUseCase,
	private val createPaypalPaymentUseCase: CreatePaypalPaymentUseCase,
	private val paypalPaymentSuccessUseCase: PaypalPaymentSuccessUseCase,
	private val paypalPaymentCancelUseCase: PaypalPaymentCancelUseCase
):ViewModel() {
	private val _checkoutState = MutableStateFlow<ScreenState<UiCheckout>>(ScreenState.Loading)
	private val _selectedPaymentMethod = MutableStateFlow<UiPaymentMethod?>(null)
	private val _actionState = MutableStateFlow(NetworkActionState())
	private val _paypalPaymentUrl = MutableStateFlow("")
	private val _orderSuccess = MutableStateFlow(false)
	private val _orderMessage = MutableStateFlow("")

	val checkoutState =_checkoutState.asStateFlow()
	val selectedPaymentMethod = _selectedPaymentMethod.asStateFlow()
	val actionState = _actionState.asStateFlow()
	val paypalPaymentUrl = _paypalPaymentUrl.asStateFlow()
	val orderSuccess = _orderSuccess.asStateFlow()
	val orderMessage = _orderMessage.asStateFlow()


	init {
		getCheckout()
	}

	fun onRetry(){
		getCheckout()
	}

	fun onSelectPaymentMethod(method: UiPaymentMethod){
		_selectedPaymentMethod.value = method
	}

	fun resetActionState(){
		_actionState.value = NetworkActionState()
	}

	fun onPlaceOrder(){
		placeOrder()
	}

	fun onCreatePaypalPayment(){
		createPaypalPayment()
	}

	fun onPaypalPaymentSuccess(token: String,payerId: String){
		paypalPaymentSuccess(token, payerId)
	}

	fun onPaypalPaymentCancel(){
		paypalPaymentCancel()
	}

	fun clearPaypalPaymentUrl(){
		_paypalPaymentUrl.value = ""
	}

	private fun getCheckout(){
		checkoutUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_checkoutState.value = ScreenState.Error(it.exception.message.toString())
				}
				is NetworkResponseState.Loading -> {
					_checkoutState.value = ScreenState.Loading
				}
				is NetworkResponseState.Success -> {
					_checkoutState.value = ScreenState.Success(checkoutApiToUiMapper.map(it.result))

					_selectedPaymentMethod.value = (_checkoutState.value as ScreenState.Success<UiCheckout>).uiData.payment.firstOrNull()
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun placeOrder(){
		placeOrderUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_actionState.value = _actionState.value.copy(
						isError = true,
						isLoading = false,
						message = it.exception.message.toString()
					)
				}
				is NetworkResponseState.Loading -> {
					_actionState.value = _actionState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					_actionState.value = _actionState.value.copy(isLoading = false,)
					_orderSuccess.value = true
					_orderMessage.value = it.result
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun createPaypalPayment(){
		createPaypalPaymentUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_actionState.value = _actionState.value.copy(
						isError = true,
						isLoading = false,
						message = it.exception.message.toString()
					)
				}
				is NetworkResponseState.Loading -> {
					_actionState.value = _actionState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					_actionState.value = _actionState.value.copy(isLoading = false)
					_paypalPaymentUrl.value = it.result
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun paypalPaymentSuccess(token: String,payerId: String){
		paypalPaymentSuccessUseCase(token, payerId).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					Log.e("CHECKOUT_VM", it.exception.toString())
					_actionState.value = _actionState.value.copy(
						isError = true,
						isLoading = false,
						message = it.exception.message.toString()
					)
				}
				is NetworkResponseState.Loading -> {
					_actionState.value = _actionState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					_actionState.value = _actionState.value.copy(isLoading = false)
					_orderSuccess.value = true
					_orderMessage.value = it.result
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun paypalPaymentCancel(){
		paypalPaymentCancelUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_actionState.value = _actionState.value.copy(
						isError = true,
						isLoading = false,
						message = it.exception.message.toString()
					)
				}
				is NetworkResponseState.Loading -> {
					_actionState.value = _actionState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					_actionState.value = _actionState.value.copy(
						isSuccess = true,
						isLoading = false,
						message = it.result
					)
				}
			}
		}.launchIn(viewModelScope)
	}

}