package com.example.deniseshop.core.domain.usecase

import androidx.core.text.isDigitsOnly
import com.example.deniseshop.core.domain.model.ValidationError
import com.example.deniseshop.core.domain.model.ValidationResult
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor(){
	operator fun invoke(
		password: String,
		min: Int = 6,
		max: Int = 255
	): ValidationResult {
		return if(password.isEmpty()) {
			ValidationResult(success = false, error = ValidationError.PASSWORD_REQUIRED)
		}else if(password.length !in min..max ) {
			ValidationResult(success = false, error = ValidationError.SHORT_PASSWORD)
		} else if (password.isDigitsOnly()){
			ValidationResult(success = false, error = ValidationError.NOT_ALPHA_NUMERIC)
		}else{
			ValidationResult(success = true)
		}
	}
}