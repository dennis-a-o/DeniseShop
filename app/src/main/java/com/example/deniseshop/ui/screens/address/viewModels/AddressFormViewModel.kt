package com.example.deniseshop.ui.screens.address.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiAddress
import com.example.deniseshop.domain.usercase.ValidateInputUseCase
import com.example.deniseshop.domain.usercase.address.AddAddressUseCase
import com.example.deniseshop.domain.usercase.address.GetCountriesUseCase
import com.example.deniseshop.domain.usercase.address.UpdateAddressUseCase
import com.example.deniseshop.ui.models.AddressFormState
import com.example.deniseshop.ui.models.NetworkActionState
import com.example.deniseshop.ui.models.UiAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddressFormViewModel @Inject constructor(
	private val addAddressUseCase: AddAddressUseCase,
	private val updateAddressUseCase: UpdateAddressUseCase,
	private val validateInputUseCase: ValidateInputUseCase,
	private val getCountriesUseCase: GetCountriesUseCase,
	savedStateHandle: SavedStateHandle
): ViewModel() {
	private val  _address: UiAddress =  savedStateHandle.toRoute()
	private val _isEditing = MutableStateFlow(value = _address.id > 0)
	private val _addressFormState = MutableStateFlow(AddressFormState())
	private val _countries = MutableStateFlow(emptyList<String>())
	private val _actionState = MutableStateFlow(NetworkActionState())

	val isEditing = _isEditing.asStateFlow()
	val addressFormState =_addressFormState.asStateFlow()
	val countries = _countries.asStateFlow()
	val actionState = _actionState.asStateFlow()

	init {
		initState()
		getCountryList()
	}

	fun resetActionState(){
		_actionState.value = NetworkActionState()
	}

	fun onEvent(event:AddressFormEvent){
		when(event){
			is AddressFormEvent.AddressChanged -> {
				_addressFormState.value = _addressFormState.value.copy(address = event.address)
			}
			is AddressFormEvent.AddressTypeChanged -> {
				_addressFormState.value = _addressFormState.value.copy(type = event.type)
			}
			is AddressFormEvent.CityChanged -> {
				_addressFormState.value = _addressFormState.value.copy(city = event.city)
			}
			is AddressFormEvent.CountryChanged -> {
				_addressFormState.value = _addressFormState.value.copy(country = event.country)
			}
			is AddressFormEvent.EmailChanged ->{
				_addressFormState.value = _addressFormState.value.copy(email = event.email)
			}
			is AddressFormEvent.IsDefaultChanged -> {
				_addressFormState.value = _addressFormState.value.copy(default = event.isDefault)
			}
			is AddressFormEvent.NameChanged -> {
				_addressFormState.value = _addressFormState.value.copy(name = event.name)
			}
			is AddressFormEvent.PhoneChanged -> {
				_addressFormState.value = _addressFormState.value.copy(phone = event.phone)
			}
			is AddressFormEvent.StateChanged -> {
				_addressFormState.value = _addressFormState.value.copy(state = event.state)
			}
			is AddressFormEvent.ZipCodeChanged -> {
				_addressFormState.value = _addressFormState.value.copy(zipCode = event.zipCode)
			}
			is AddressFormEvent.Submit -> {
				validate()
			}
		}
	}

	private fun validate(){
		val nameResult = validateInputUseCase.validateName(_addressFormState.value.name)
		val emailResult = validateInputUseCase.validateEmail(_addressFormState.value.email)
		val phoneResult = validateInputUseCase.validatePhone(_addressFormState.value.phone)
		val countryResult = validateInputUseCase.validateName(_addressFormState.value.country)
		val stateResult = validateInputUseCase.validateName((_addressFormState.value.state))
		val cityResult = validateInputUseCase.validateName(_addressFormState.value.city)
		val addressResult = validateInputUseCase.validateName(_addressFormState.value.address)
		val zipResult = validateInputUseCase.validateName(_addressFormState.value.zipCode)
		val typeResult = validateInputUseCase.validateName(_addressFormState.value.type)

		_addressFormState.value =_addressFormState.value.copy(
			nameError = nameResult.errorMessage,
			emailError = emailResult.errorMessage,
			phoneError = phoneResult.errorMessage,
			countryError = countryResult.errorMessage,
			stateError = stateResult.errorMessage,
			cityError = cityResult.errorMessage,
			addressError = addressResult.errorMessage,
			zipCodeError = zipResult.errorMessage,
			typeError = typeResult.errorMessage
		)

		if (
			nameResult.successful &&
			emailResult.successful &&
			phoneResult.successful &&
			countryResult.successful &&
			stateResult.successful &&
			cityResult.successful &&
			addressResult.successful &&
			zipResult.successful &&
			typeResult.successful
		){
			if(isEditing.value){
				updateAddress()
			}else{
				addAddress()
			}
		}
	}

	private fun updateAddress(){
		updateAddressUseCase(
			ApiAddress(
				id = _address.id,
				useId = _address.useId,
				name = addressFormState.value.name,
				email = addressFormState.value.email,
				phone = addressFormState.value.phone,
				country = addressFormState.value.country,
				state = addressFormState.value.state,
				city = addressFormState.value.city,
				address = addressFormState.value.address,
				zipCode = addressFormState.value.zipCode,
				default = addressFormState.value.default,
				type = addressFormState.value.type
			)
		).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isError = true,
						message = it.exception.message.toString()
					)
					_addressFormState.value = _addressFormState.value.copy(isLoading = false)
				}
				is NetworkResponseState.Loading -> {
					_addressFormState.value = _addressFormState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isSuccess = true,
						message = it.result
					)
					_addressFormState.value = _addressFormState.value.copy(isLoading = false)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun addAddress(){
		addAddressUseCase(
			ApiAddress(
				id = 0,
				useId = 0,
				name = addressFormState.value.name,
				email = addressFormState.value.email,
				phone = addressFormState.value.phone,
				country = addressFormState.value.country,
				state = addressFormState.value.state,
				city = addressFormState.value.city,
				address = addressFormState.value.address,
				zipCode = addressFormState.value.zipCode,
				default = addressFormState.value.default,
				type = addressFormState.value.type
			)
		).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isError = true,
						message = it.exception.message.toString()
					)
					_addressFormState.value = _addressFormState.value.copy(isLoading = false)
				}
				is NetworkResponseState.Loading -> {
					_addressFormState.value = _addressFormState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					_actionState.value = _actionState.value.copy(
						isLoading = false,
						isSuccess = true,
						message = it.result
					)
					_addressFormState.value = _addressFormState.value.copy(isLoading = false)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun getCountryList(){
		getCountriesUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {}
				is NetworkResponseState.Loading -> {}
				is NetworkResponseState.Success -> {
					_countries.value = it.result
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun initState(){
		_addressFormState.value = _addressFormState.value.copy(
			name = _address.name,
			email = _address.email,
			phone = _address.phone,
			country = _address.country,
			state = _address.state,
			city = _address.city,
			address = _address.address,
			zipCode = _address.zipCode,
			type = _address.type,
			default = _address.default
		)
	}
}