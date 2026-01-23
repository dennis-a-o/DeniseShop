package com.example.deniseshop.feature.addresses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.Address
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.core.presentation.ScreenState
import com.example.deniseshop.core.presentation.UiText
import com.example.deniseshop.core.presentation.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressesViewModel @Inject constructor(
	private val shopRepository: ShopRepository
): ViewModel() {
	private val _state = MutableStateFlow<ScreenState<List<Address>>>(ScreenState.Loading)
	private var _error = MutableStateFlow<UiText?>(null)

	val state = _state.asStateFlow()
	val error = _error.asStateFlow()

	init {
		getAddresses()
	}

	fun retry(){
		getAddresses()
	}

	fun clearError(){
		_error.value = null
	}


	fun setDefaultAddress(id: Long){
		viewModelScope.launch {
			shopRepository.setDefaultAddress(id)
				.onSuccess {
					getAddresses()
				}
				.onError{
					_error.value = it.toUiText()
				}
		}
	}

	fun removeAddress(id: Long){
		viewModelScope.launch {
			shopRepository.deleteAddress(id)
				.onSuccess {
					getAddresses()
				}
				.onError{
					_error.value = it.toUiText()
				}
		}
	}

	private fun getAddresses(){
		viewModelScope.launch {
			_state.value = ScreenState.Loading

			shopRepository.getAddresses()
				.onSuccess {
					_state.value = ScreenState.Success(it)
				}
				.onError {
					_state.value = ScreenState.Error(it.toUiText())
				}
		}
	}
}