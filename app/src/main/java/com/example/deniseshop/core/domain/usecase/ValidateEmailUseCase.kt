package com.example.deniseshop.core.domain.usecase

import com.example.deniseshop.core.domain.model.ValidationResult
import com.example.deniseshop.core.domain.model.ValidationError
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor(){
	companion object{
		private val EMAIL_REGEX = Regex(
			"^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-])*@" +
					"(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
		)
	}
	operator fun invoke(email: String): ValidationResult {
		return if(email.isEmpty()){
			ValidationResult(success = false, error = ValidationError.EMAIL_REQUIRED)
		}else if(!(EMAIL_REGEX.matches(email))){
			ValidationResult(success = false, error = ValidationError.INVALID_EMAIL)
		}else{
			ValidationResult(success = true)
		}
	}
}