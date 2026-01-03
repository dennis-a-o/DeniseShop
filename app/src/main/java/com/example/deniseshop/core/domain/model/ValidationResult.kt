package com.example.deniseshop.core.domain.model

data class ValidationResult(
	val success: Boolean = false,
	val error: ValidationError = ValidationError.INVALID
)
