package com.example.deniseshop.feature.addeditaddress

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.Address
import com.example.deniseshop.core.domain.model.AddressType
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.core.domain.usecase.ValidateEmailUseCase
import com.example.deniseshop.core.domain.usecase.ValidateGeneralInputUseCase
import com.example.deniseshop.core.domain.usecase.ValidateNameUseCase
import com.example.deniseshop.core.domain.usecase.ValidatePhoneUseCase
import com.example.deniseshop.core.presentation.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditAddressViewModel @Inject constructor(
	private val shopRepository: ShopRepository,
	private val validateName: ValidateNameUseCase,
	private val validatePhone: ValidatePhoneUseCase,
	private val validateEmail: ValidateEmailUseCase,
	private val validateGeneralInput: ValidateGeneralInputUseCase,
	savedStateHandle: SavedStateHandle,
): ViewModel() {
	private val addressId =  savedStateHandle["addressId"] ?: 0L
	private val _state = MutableStateFlow(AddEditAddressState())

	val state = _state.asStateFlow()

	init {
		if (addressId != 0L){
			getAddress()
		}
		getCountries()
	}

	fun onEvent(event: AddEditAddressEvent){
		when(event) {
			is AddEditAddressEvent.AddressChange -> {
				_state.update { it.copy(address = event.address) }
			}
			is AddEditAddressEvent.AddressTypeChange -> {
				_state.update { it.copy(type = event.type) }
			}
			is AddEditAddressEvent.CityChange -> {
				_state.update { it.copy(city = event.city) }
			}
			AddEditAddressEvent.ClearError -> {
				_state.update { it.copy(error = null) }
			}
			is AddEditAddressEvent.CountryChange -> {
				_state.update { it.copy(country = event.country) }
			}
			is AddEditAddressEvent.EmailChange -> {
				_state.update { it.copy(email = event.email) }
			}
			is AddEditAddressEvent.IsDefaultChange -> {
				_state.update { it.copy(default = event.isDefault) }
			}
			is AddEditAddressEvent.NameChange -> {
				_state.update { it.copy(name = event.name) }
			}
			is AddEditAddressEvent.PhoneChange -> {
				_state.update { it.copy(phone = event.phone) }
			}
			is AddEditAddressEvent.StateChange -> {
				_state.update { it.copy(state = event.state) }
			}
			AddEditAddressEvent.Submit -> validate()
			is AddEditAddressEvent.ZipCodeChange -> {
				_state.update { it.copy(zipCode = event.zipCode) }
			}
			AddEditAddressEvent.ClearMessage -> {
				_state.update { it.copy(success = null) }
			}
		}
	}

	private fun validate(){
		val nameResult = validateName(_state.value.name)
		val emailResult = validateEmail(_state.value.email)
		val phoneResult = validatePhone(_state.value.phone)
		val countryResult = validateGeneralInput(_state.value.country)
		val stateResult = validateGeneralInput(_state.value.state)
		val cityResult = validateGeneralInput(_state.value.city)
		val addressResult = validateGeneralInput(_state.value.address)
		val zipCodeResult = validateGeneralInput(_state.value.zipCode)

		_state.update {
			it.copy(
				nameError = nameResult.error?.toUiText(),
				emailError = emailResult.error?.toUiText(),
				phoneError = phoneResult.error?.toUiText(),
				countryError = countryResult.error?.toUiText(),
				stateError = stateResult.error?.toUiText(),
				cityError = cityResult.error?.toUiText(),
				addressError = addressResult.error?.toUiText(),
				zipCodeError = zipCodeResult.error?.toUiText(),
			)
		}

		if (
			nameResult.success &&
			emailResult.success &&
			phoneResult.success &&
			countryResult.success &&
			stateResult.success &&
			cityResult.success &&
			addressResult.success &&
			zipCodeResult.success
		){
			if (_state.value.theAddress != null){
				update()
			}else{
				add()
			}
		}
	}

	private fun update(){
		viewModelScope.launch {
			_state.update { it.copy(isLoading = true) }

			shopRepository.updateAddress(
				Address(
					id = addressId,
					useId = _state.value.theAddress?.useId ?: 0L,
					name = _state.value.name,
					email = _state.value.email,
					phone = _state.value.phone,
					country = _state.value.country,
					state = _state.value.state,
					city = _state.value.city,
					address = _state.value.address,
					zipCode = _state.value.zipCode,
					type = _state.value.type,
					default = _state.value.default,
				)
			)
				.onSuccess { msg ->
					_state.update { it.copy(success = msg, isLoading = false ) }
				}
				.onError { error ->
					_state.update { it.copy(error = error.toUiText(), isLoading = false ) }
				}
		}
	}

	private fun add(){
		viewModelScope.launch {
			_state.update { it.copy(isLoading = true) }

			shopRepository.addAddress(
				Address(
					id = 0L,
					useId =  0L,
					name = _state.value.name,
					email = _state.value.email,
					phone = _state.value.phone,
					country = _state.value.country,
					state = _state.value.state,
					city = _state.value.city,
					address = _state.value.address,
					zipCode = _state.value.zipCode,
					type = _state.value.type,
					default = _state.value.default,
				)
			)
				.onSuccess { msg ->
					_state.update { it.copy(success = msg, isLoading = false ) }
				}
				.onError { error ->
					_state.update { it.copy(error = error.toUiText() , isLoading = false) }
				}
		}
	}

	private fun getAddress(){
		viewModelScope.launch {
			shopRepository.getAddress(addressId)
				.onSuccess { address ->
					_state.update {
						it.copy(
							theAddress = address,
							name = address?.name ?: "",
							email = address?.email ?: "",
							phone = address?.phone ?: "",
							country = address?.country ?: "",
							state = address?.state ?: "",
							city = address?.city ?: "",
							address = address?.address ?: "",
							zipCode = address?.zipCode ?: "",
							type = address?.type ?: AddressType.Other,
							default = address?.default ?: false,
						)
					}
				}
				.onError { error ->
					_state.update { it.copy(error = error.toUiText()) }
				}
		}
	}

	private fun getCountries(){
		viewModelScope.launch {
			shopRepository.getCountries()
				.onSuccess { data ->
					_state.update {
						it.copy(countries = data)
					}
				}
				.onError { error ->
					_state.update { it.copy(error = error.toUiText()) }
				}
		}
	}
}