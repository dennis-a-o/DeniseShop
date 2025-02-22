package com.example.deniseshop.utils

import android.util.Patterns
import androidx.core.text.isDigitsOnly

object ValidationUtil {
	fun isValidNumber(number: String): Boolean{
		return number.isDigitsOnly()
	}

	fun isEmailValid(email: String): Boolean {
		return Patterns.EMAIL_ADDRESS.matcher(email).matches()
	}

	fun isPhoneValid(phone: String): Boolean{
		return Patterns.PHONE.matcher(phone).matches()
	}

	fun isPasswordValid(password: String): Boolean {
		return  password.any(){ it.isDigit() } && password.any(){ it.isLetter() }
	}
}