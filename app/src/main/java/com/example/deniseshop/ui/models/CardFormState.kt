package com.example.deniseshop.ui.models

data class CardFormState(
	val type: String = "",
	val typeError: String? = null,
	val firstName: String = "",
	val firstNameError: String? = null,
	val lastName: String = "",
	val lastNameError: String? = null,
	val number: Long = 0,
	val numberError: String? = null,
	val expiryMonth: Int = 0,
	val expiryMonthError: String? = null,
	val expiryYear: Int = 0,
	val expiryYearError: String? = null,
	val cvv: Int = 0,
	val cvvError: String? = null,
	val isLoading: Boolean = false
)
