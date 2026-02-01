package com.example.deniseshop.feature.addeditaddress

import com.example.deniseshop.core.domain.model.Address
import com.example.deniseshop.core.domain.model.AddressType
import com.example.deniseshop.core.presentation.UiText

data class AddEditAddressState(
	val theAddress: Address? = null,
	val name:String = "",
	val nameError: UiText? = null,
	val email:String = "",
	val emailError: UiText? = null,
	val phone:String = "",
	val phoneError: UiText? = null,
	val country:String = "",
	val countryError: UiText? = null,
	val state:String = "",
	val stateError: UiText? = null,
	val city:String = "",
	val cityError: UiText? = null,
	val address:String = "",
	val addressError: UiText? = null,
	val zipCode:String = "",
	val zipCodeError: UiText? = null,
	val type: AddressType= AddressType.Other,
	val typeError: UiText? = null,
	val default :Boolean = false,
	val isLoading: Boolean = false,
	val error: UiText? = null,
	val success: String? = null,
	val countries:List<String> = emptyList()
)
