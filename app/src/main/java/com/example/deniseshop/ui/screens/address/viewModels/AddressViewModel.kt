package com.example.deniseshop.ui.screens.address.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.domain.usercase.address.GetAllAddressUseCase
import com.example.deniseshop.domain.usercase.address.RemoveAddressUseCase
import com.example.deniseshop.domain.usercase.address.SetAddressDefaultUseCase
import com.example.deniseshop.ui.mapper.AddressListApiToUiMapper
import com.example.deniseshop.ui.models.NetworkActionState
import com.example.deniseshop.ui.models.UiAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
	private val getAllAddressUseCase: GetAllAddressUseCase,
	private val setAddressDefaultUseCase: SetAddressDefaultUseCase,
	private val removeAddressUseCase: RemoveAddressUseCase,
	private val addressListApiToUiMapper: AddressListApiToUiMapper
): ViewModel() {
	private val _addressState = MutableStateFlow<ScreenState<List<UiAddress>>>(ScreenState.Loading)
	private val _actionState = MutableStateFlow(NetworkActionState())

	val addressState = _addressState.asStateFlow()
	val actionState = _actionState.asStateFlow()

	init {
		getAllAddress()
	}

	fun resetActionState(){
		_actionState.value = NetworkActionState()
	}

	fun onRetry(){
		getAllAddress()
	}

	fun onSetAddressDefault(id: Long){
		setAddressDefault(id)
	}

	fun onRemoveAddress(id: Long){
		removeAddress(id)
	}

	private fun getAllAddress(){
		getAllAddressUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_addressState.value = ScreenState.Error(it.exception.message.toString())
				}
				is NetworkResponseState.Loading -> {
					_addressState.value = ScreenState.Loading
				}
				is NetworkResponseState.Success -> {
					_addressState.value = ScreenState.Success(addressListApiToUiMapper.map(it.result))
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun setAddressDefault(id: Long){
		setAddressDefaultUseCase(id).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isError = true
					)
				}
				is NetworkResponseState.Loading -> {
					_actionState.value = _actionState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					getAllAddress()

					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isSuccess = true,
						message = it.result
						)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun removeAddress(id: Long){
		removeAddressUseCase(id).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isError = true
					)
				}
				is NetworkResponseState.Loading -> {
					_actionState.value = _actionState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					getAllAddress()

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