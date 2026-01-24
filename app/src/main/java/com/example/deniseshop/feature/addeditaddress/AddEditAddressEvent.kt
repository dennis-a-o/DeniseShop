package com.example.deniseshop.feature.addeditaddress

import com.example.deniseshop.core.domain.model.AddressType

sealed interface AddEditAddressEvent {
	data class NameChange(val name: String): AddEditAddressEvent
	data class EmailChange(val email: String): AddEditAddressEvent
	data class PhoneChange(val phone: String): AddEditAddressEvent
	data class CountryChange(val country: String): AddEditAddressEvent
	data class StateChange(val state: String): AddEditAddressEvent
	data class CityChange(val city: String): AddEditAddressEvent
	data class AddressChange(val address:String): AddEditAddressEvent
	data class ZipCodeChange(val zipCode:String): AddEditAddressEvent
	data class AddressTypeChange(val type: AddressType): AddEditAddressEvent
	data class IsDefaultChange(val isDefault:Boolean): AddEditAddressEvent
	data object ClearError:  AddEditAddressEvent
	data object ClearMessage:  AddEditAddressEvent
	data object Submit: AddEditAddressEvent
}