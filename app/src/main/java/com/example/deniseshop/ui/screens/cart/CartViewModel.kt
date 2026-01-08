package com.example.deniseshop.ui.screens.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.data.models.ApiCart
import com.example.deniseshop.domain.usercase.GetLoggedInStateUseCase
import com.example.deniseshop.domain.usercase.cart.AddCartUseCase
import com.example.deniseshop.domain.usercase.cart.ApplyCouponUseCase
import com.example.deniseshop.domain.usercase.cart.ClearCartUseCase
import com.example.deniseshop.domain.usercase.cart.ClearCouponUseCase
import com.example.deniseshop.domain.usercase.cart.DecreaseQuantityUseCase
import com.example.deniseshop.domain.usercase.cart.GetCartUseCase
import com.example.deniseshop.domain.usercase.cart.IncreaseQuantityUseCase
import com.example.deniseshop.domain.usercase.cart.RemoveCartUseCase
import com.example.deniseshop.ui.mapper.BaseMapper
import com.example.deniseshop.ui.models.NetworkActionState
import com.example.deniseshop.ui.models.UiCart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
	private val getCartUseCase: GetCartUseCase,
	private  val addCartUseCase: AddCartUseCase,
	private val removeCartUseCase: RemoveCartUseCase,
	private val increaseQuantityUseCase: IncreaseQuantityUseCase,
	private val decreaseQuantityUseCase: DecreaseQuantityUseCase,
	private val clearCartUseCase: ClearCartUseCase,
	private val getLoggedInStateUseCase: GetLoggedInStateUseCase,
	private val applyCouponUseCase: ApplyCouponUseCase,
	private val clearCouponUseCase: ClearCouponUseCase,
	private val cartApiToUiMapper: BaseMapper<ApiCart, UiCart>
): ViewModel() {
	private val _cartState = MutableStateFlow<ScreenState<UiCart>>(ScreenState.Loading)
	private val _isLoggedIn = MutableStateFlow(false)
	private val _cartCountState = MutableStateFlow(value = 0)
	private val _cartItems = MutableStateFlow(setOf<Long>())
	private val _actionState = MutableStateFlow(NetworkActionState())

	val cartState = _cartState.asStateFlow()
	val isLoggedIn = _isLoggedIn.asStateFlow()
	val cartCountState = _cartCountState.asStateFlow()
	val cartItems = _cartItems.asStateFlow()
	val actionState = _actionState.asStateFlow()

	init{
		checkIsLoggedIn()
		getCart()
	}

	fun onToggleCart(productId: Long){
		if (cartItems.value.contains(productId)){
			removeCart(productId)
		}else{
			addCart(productId, null, null, null)
		}

	}

	fun onAddCart(
		productId: Long,
		quantity: Int?= null,
		color: String? = null,
		size: String? = null
	){
		addCart(productId, quantity, color, size)
	}

	fun onRemoveCart(productId: Long){
		removeCart(productId)
	}

	fun onClearCart(){
		clearCart()
	}

	fun onApplyCoupon(coupon: String){
		applyCoupon(coupon)
	}

	fun onClearCoupon(){
		clearCoupon()
	}

	fun onIncreaseQuantity(id: Long){
		increaseQuantity(id)
	}

	fun onDecreaseQuantity(id: Long){
		decreaseQuantity(id)
	}

	fun onRetry(){
		getCart()
	}

	fun resetActionState(){
		_actionState.value = NetworkActionState()
	}

	private fun checkIsLoggedIn(){
		getLoggedInStateUseCase().onEach {
			_isLoggedIn.value = it
		}.launchIn(viewModelScope)
	}

	private fun getCart(){
		/*getCartUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_cartState.value = ScreenState.Error(it.exception.message.toString())
				}
				is NetworkResponseState.Loading -> {
					_cartState.value = ScreenState.Loading
				}
				is NetworkResponseState.Success -> {
					_cartState.value = ScreenState.Success(cartApiToUiMapper.map(it.result))

					_cartItems.value = it.result.cartItems.map { item ->  item.productId }.toSet()

					_cartCountState.value = it.result.cartItems.map { item -> item.quantity }.reduceOrNull { acc, i ->  acc + i }?: 0
				}
			}
		}.launchIn(viewModelScope)*/
	}

	private fun addCart(productId: Long, quantity: Int?,color: String?, size: String?){
		addCartUseCase(
			product = productId,
			quantity = quantity,
			color = color,
			size = size
		).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isError = true,
						message = it.exception.message.toString()
					)
				}
				is  NetworkResponseState.Loading -> {
					_actionState.value = _actionState.value.copy(isLoading = false)
				}
				is NetworkResponseState.Success -> {
					getCart()

					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isSuccess = true,
						message = it.result
					)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun removeCart(productId: Long){
		removeCartUseCase(productId).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isError = true,
						message = it.exception.message.toString()
					)
				}
				is  NetworkResponseState.Loading -> {
					_actionState.value = _actionState.value.copy(isLoading = false)
				}
				is NetworkResponseState.Success -> {
					getCart()

					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isSuccess = true,
						message = it.result
					)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun increaseQuantity(id: Long){
		increaseQuantityUseCase(id).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isError = true,
						message = it.exception.message.toString()
					)
				}
				is  NetworkResponseState.Loading -> {
					_actionState.value = _actionState.value.copy(isLoading = false)
				}
				is NetworkResponseState.Success -> {
					getCart()

					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isSuccess = true,
						message = it.result
					)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun decreaseQuantity(id: Long){
		decreaseQuantityUseCase(id).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isError = true,
						message = it.exception.message.toString()
					)
				}
				is  NetworkResponseState.Loading -> {
					_actionState.value = _actionState.value.copy(isLoading = false)
				}
				is NetworkResponseState.Success -> {
					getCart()

					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isSuccess = true,
						message = it.result
					)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun clearCart(){
		clearCartUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isError = true,
						message = it.exception.message.toString()
					)
				}
				is  NetworkResponseState.Loading -> {
					_actionState.value = _actionState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					getCart()
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isSuccess = true,
						message = it.result
					)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun applyCoupon(coupon: String){
		applyCouponUseCase(coupon).onEach{
			when(it){
				is NetworkResponseState.Error -> {
					Log.e("CART_VM", it.exception.toString())
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isError = true,
						message = it.exception.message.toString()
					)
				}
				is  NetworkResponseState.Loading -> {
					_actionState.value = _actionState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					getCart()
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isSuccess = true,
						message = it.result
					)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun clearCoupon(){
		clearCouponUseCase().onEach{
			when(it){
				is NetworkResponseState.Error -> {
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isError = true,
						message = it.exception.message.toString()
					)
				}
				is  NetworkResponseState.Loading -> {
					_actionState.value = _actionState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					getCart()
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isSuccess = true,
						message = it.result
					)
				}
			}
		}.launchIn(viewModelScope)
	}
}