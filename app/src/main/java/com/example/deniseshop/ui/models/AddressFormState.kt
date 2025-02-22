package com.example.deniseshop.ui.models

data class AddressFormState(
	val name:String = "",
	val nameError:String? = null,
	val email:String = "",
	val emailError:String? = null,
	val phone:String = "",
	val phoneError:String? = null,
	val country:String = "",
	val countryError:String? = null,
	val state:String = "",
	val stateError:String? = null,
	val city:String = "",
	val cityError:String? = null,
	val address:String = "",
	val addressError:String? = null,
	val zipCode:String = "",
	val zipCodeError:String? = null,
	val type:String = "",
	val typeError:String? = null,
	val default :Boolean= false,
	val defaultError:String? = null,
	val isLoading:Boolean = false
)
