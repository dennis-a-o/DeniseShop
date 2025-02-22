package com.example.deniseshop.domain.usercase

import com.example.deniseshop.ui.models.ValidationResult

interface ValidateInputUseCase{
	fun validateName(name: String): ValidationResult
	fun validateEmail(email: String): ValidationResult
	fun validatePhone(phone :String): ValidationResult
	fun validatePassword(password: String):ValidationResult
	fun validateAcceptTerms(accept: Boolean): ValidationResult
	fun validateNumber(number: Long, min: Long = Long.MIN_VALUE,  max: Long = Long.MAX_VALUE): ValidationResult
	fun validateString(value: String, min: Int = Int.MIN_VALUE,  max: Int = Int.MAX_VALUE): ValidationResult
}