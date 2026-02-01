package com.example.deniseshop.feature.orderdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.core.presentation.UiText
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

@HiltViewModel(assistedFactory = OrderDetailViewModel.Factory::class)
class OrderDetailViewModel @AssistedInject constructor(
	private val shopRepository: ShopRepository,
	@Assisted val navKey: Route.OrderDetail
): ViewModel() {
	private val orderId = navKey.id
	private val _state = MutableStateFlow(OrderDetailState())

	val state = _state.asStateFlow()

	init {
		getOrderDetail()
	}

	fun retry(){
		getOrderDetail()
	}

	fun clearErrorSuccessState(){
		_state.update {
			it.copy(
				error = null,
				success = null
			)
		}
	}

	fun onReview(
		itemId: Long,
		rating: Int,
		review: String
	){
		viewModelScope.launch {
			_state.update { it.copy(isSubmittingReview = true) }

			shopRepository.addReview(
				orderItemId = itemId,
				rating = rating,
				review = review
			)
				.onSuccess { msg ->
					_state.update {
						it.copy(
							isSubmittingReview = false,
							success = msg
						)
					}
				}
				.onError { error ->
					_state.update {
						it.copy(
							isSubmittingReview = false,
							error = error.toUiText()
						)
					}
				}
		}
	}

	fun downloadOrderItem(itemId: Long){
		viewModelScope.launch {
			_state.update { it.copy(isDownloadingItem = true) }

			shopRepository.downloadItem(itemId)
				.onSuccess { msg ->
					_state.update {
						it.copy(
							isDownloadingItem = false,
							success = msg
						)
					}
				}
				.onFailure { error ->
					_state.update {
						it.copy(
							isDownloadingItem = false,
							error = UiText.DynamicString(error.message ?: "Unknown error")
						)
					}
				}
		}
	}

	fun downloadInvoice(){
		viewModelScope.launch {
			_state.update { it.copy(isDownloadingInvoice = true) }

			shopRepository.downloadInvoice(orderId)
				.onSuccess { msg ->
					_state.update {
						it.copy(
							isDownloadingInvoice = false,
							success = msg
						)
					}
				}
				.onFailure { error ->
					_state.update {
						it.copy(
							isDownloadingInvoice = false,
							error = UiText.DynamicString(error.message ?: "Unknown error")
						)
					}
				}
		}
	}

	private fun getOrderDetail(){
		viewModelScope.launch {
			_state.update { it.copy(isLoading = true) }

			shopRepository.getOrderDetail(orderId)
				.onSuccess { orderDetail ->
					_state.update {
						it.copy(
							isLoading = false,
							orderDetail = orderDetail
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
		fun create(navKey: Route.OrderDetail): OrderDetailViewModel
	}
}