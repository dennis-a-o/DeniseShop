package com.example.deniseshop.core.domain.usecase

import android.util.Patterns
import com.example.deniseshop.core.domain.model.ValidationError
import com.example.deniseshop.core.domain.model.ValidationResult
import javax.inject.Inject

class ValidatePhoneUseCase @Inject constructor(){
	operator fun invoke(
		phone: String
	): ValidationResult {
		return if(phone.isEmpty()) {
			ValidationResult(success = false, error = ValidationError.PHONE_REQUIRED)
		} else if (!Patterns.PHONE.matcher(phone).matches()){
			ValidationResult(success = false, error = ValidationError.INVALID_PHONE)
		}else{
			ValidationResult(success = true)
		}
	}
}