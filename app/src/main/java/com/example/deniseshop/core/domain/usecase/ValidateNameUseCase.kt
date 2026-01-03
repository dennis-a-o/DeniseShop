package com.example.deniseshop.core.domain.usecase

import com.example.deniseshop.core.domain.model.ValidationError
import com.example.deniseshop.core.domain.model.ValidationResult
import javax.inject.Inject

class ValidateNameUseCase @Inject constructor() {
	operator fun invoke(
		string: String,
		min: Int = 1,
		max: Int = Int.MAX_VALUE
	): ValidationResult {
		return if(string.isEmpty()){
			ValidationResult(success = false, error = ValidationError.NAME_REQUIRED)
		}else if(string.length !in min..max){
			ValidationResult(success = false, error = ValidationError.INVALID_NAME)
		} else {
			ValidationResult(success = true)
		}
	}
}