package com.example.deniseshop.ui.screens.product.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.data.models.ApiProductDetail
import com.example.deniseshop.domain.usercase.product.GetProductDetailUseCase
import com.example.deniseshop.domain.usercase.product.SetViewedUseCase
import com.example.deniseshop.ui.mapper.BaseMapper
import com.example.deniseshop.ui.models.UiProductDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
	private val getProductDetailUseCase: GetProductDetailUseCase,
	private val productDetailAPiToUiMapper: BaseMapper<ApiProductDetail, UiProductDetail>,
	private val setViewedUseCase: SetViewedUseCase,
	savedStateHandle: SavedStateHandle
):ViewModel() {
	private val productId: Long = savedStateHandle["productId"]?: 0L
	private val _productDetailState = MutableStateFlow<ScreenState<UiProductDetail>>(ScreenState.Loading)
	private val _isProductDescriptionVisible = MutableStateFlow(false)
	private val _selectedQuantity = MutableStateFlow(1)
	private val _selectedColor = MutableStateFlow("")
	private val _selectedSize = MutableStateFlow("")
	private var _stockQuantity = 0

	val isProductDescriptionVisible = _isProductDescriptionVisible.asStateFlow()
	val productDetailState = _productDetailState.asStateFlow()
	val quantity = _selectedQuantity.asStateFlow()
	val color = _selectedColor.asStateFlow()
	val size = _selectedSize.asStateFlow()

	init {
		getProductDetail()
	}

	fun onRetry(){
		getProductDetail()
	}

	fun onEvent(event: ProductDetailEvent){
		when(event){
			is ProductDetailEvent.AddQuantity -> {
				if ((event.quantity + 1) < _stockQuantity) {
					_selectedQuantity.value = event.quantity + 1
				}
			}
			is ProductDetailEvent.MinusQuantity -> {
				if (event.quantity > 1) {
					_selectedQuantity.value = event.quantity - 1
				}
			}
			is ProductDetailEvent.ProductDescriptionVisible -> {
				_isProductDescriptionVisible.value = event.isVisible
			}
			is ProductDetailEvent.SelectColor -> {
				_selectedColor.value = event.color
			}
			is ProductDetailEvent.SelectSize -> {
				_selectedSize.value = event.size
			}
		}
	}

	private fun getProductDetail(){
		getProductDetailUseCase(productId).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_productDetailState.value = ScreenState.Error(it.exception.message.toString())
				}
				is NetworkResponseState.Loading -> {
					_productDetailState.value = ScreenState.Loading
				}
				is NetworkResponseState.Success -> {
					_productDetailState.value = ScreenState.Success(productDetailAPiToUiMapper.map(it.result))
					_stockQuantity = it.result.product.quantity
					_selectedSize.value = it.result.product.size?.firstOrNull() ?: ""
					_selectedColor.value = it.result.product.color?.firstOrNull()?.split("=")?.get(0) ?: ""
					setProductViewed()
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun setProductViewed(){
		setViewedUseCase(productId).onEach {
			when(it){
				is NetworkResponseState.Error -> {}
				NetworkResponseState.Loading -> {}
				is NetworkResponseState.Success -> {}
			}
		}.launchIn(viewModelScope)
	}
}