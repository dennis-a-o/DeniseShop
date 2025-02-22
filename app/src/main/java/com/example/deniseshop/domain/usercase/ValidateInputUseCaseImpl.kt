package com.example.deniseshop.domain.usercase

import android.content.Context
import android.util.Log
import com.example.deniseshop.R
import com.example.deniseshop.ui.models.ValidationResult
import com.example.deniseshop.utils.ValidationUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

class ValidateInputUseCaseImpl @Inject constructor(
	@ApplicationContext val context: Context
): ValidateInputUseCase{
	override fun validateName(name: String): ValidationResult {
		return if (name.isEmpty()){
			ValidationResult(
				successful = false,
				errorMessage = context.getString(R.string.nameRequired)
			)
		}else{
			ValidationResult(successful = true)
		}
	}

	override fun validateEmail(email: String): ValidationResult {
		return if (email.isEmpty()) {
			ValidationResult(
				successful = false,
				errorMessage = context.getString(R.string.emailRequired)
			)
		} else if (!(ValidationUtil.isEmailValid(email))) {
			ValidationResult(
				successful = false,
				errorMessage = context.getString(R.string.emailInvalid)
			)
		} else {
			ValidationResult(successful = true)
		}
	}

	override fun validatePhone(phone :String): ValidationResult {
		return if (phone.isEmpty()) {
			ValidationResult(
				successful = false,
				errorMessage = context.getString(R.string.phoneNumberRequired)
			)
		}else if(!(ValidationUtil.isPhoneValid(phone))){
			ValidationResult(
				successful = false,
				errorMessage = context.getString(R.string.phoneInvalid)
			)
		}else{
			ValidationResult(successful = true)
		}
	}
	override fun validatePassword(password: String): ValidationResult {
		return if (password.isEmpty()){
			ValidationResult(
				successful = false,
				errorMessage = context.getString(R.string.passwordRequired)
			)
		}else if(password.length < 8) {
			ValidationResult(
				successful = false,
				errorMessage = context.getString(R.string.passwordLengthShouldAtLeastEightCharacters)
			)
		}else if(!(ValidationUtil.isPasswordValid(password))){
			ValidationResult(
				successful = false,
				errorMessage = context.getString(R.string.passwordShouldContainAtLeastLetterAndDigit)
			)
		}else{
			ValidationResult(successful = true)
		}
	}

	override fun validateAcceptTerms(accept: Boolean): ValidationResult {
		return if (!accept){
			ValidationResult(
				successful = false,
				errorMessage = context.getString(R.string.acceptTermsAndConditionToSignUp)
			)
		}else{
			ValidationResult(successful = true)
		}
	}

	override fun validateNumber(number: Long, min: Long, max: Long): ValidationResult {
		return if(number < min || number > max){
			ValidationResult(
				successful = false,
				errorMessage = context.getString(R.string.invalidValue)
			)
		}else{
			ValidationResult(successful = true)
		}
	}

	override fun validateString(value: String, min: Int, max: Int): ValidationResult {
		return if (value.isEmpty()){
			ValidationResult(
				successful = false,
				errorMessage = context.getString(R.string.inputRequired)
			)
		}else if(value.length < min || value.length > max){
			ValidationResult(
				successful = false,
				errorMessage = context.getString(R.string.invalidValue)
			)
		}else{
			ValidationResult(successful = true)
		}
	}
}