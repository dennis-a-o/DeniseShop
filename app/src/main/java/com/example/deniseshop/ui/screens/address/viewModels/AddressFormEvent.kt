package com.example.deniseshop.ui.screens.address.viewModels

sealed class AddressFormEvent {
	data class NameChanged(val name: String):AddressFormEvent()
	data class EmailChanged(val email: String):AddressFormEvent()
	data class PhoneChanged(val phone: String):AddressFormEvent()
	data class CountryChanged(val country: String):AddressFormEvent()
	data class StateChanged(val state: String):AddressFormEvent()
	data class CityChanged(val city: String):AddressFormEvent()
	data class AddressChanged(val address:String):AddressFormEvent()
	data class ZipCodeChanged(val zipCode:String):AddressFormEvent()
	data class AddressTypeChanged(val type:String):AddressFormEvent()
	data class IsDefaultChanged(val isDefault:Boolean):AddressFormEvent()
	data object Submit: AddressFormEvent()
}