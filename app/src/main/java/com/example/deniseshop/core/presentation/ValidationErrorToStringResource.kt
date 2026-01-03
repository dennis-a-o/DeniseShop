package com.example.deniseshop.core.presentation

import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.ValidationError

fun ValidationError.toUiText(): UiText{
	val res = when(this) {
		ValidationError.INVALID_NAME -> R.string.error_invalid_name
		ValidationError.NAME_REQUIRED -> R.string.error_name_required
		ValidationError.INVALID_EMAIL -> R.string.error_invalid_email
		ValidationError.EMAIL_REQUIRED -> R.string.error_email_required
		ValidationError.INVALID_PASSWORD -> R.string.error_invalid_password
		ValidationError.INVALID_PHONE -> R.string.error_invalid_phone
		ValidationError.NOT_ALPHA_NUMERIC -> R.string.error_not_apha_numeric
		ValidationError.SHORT_PASSWORD -> R.string.error_short_password
		ValidationError.PASSWORD_REQUIRED -> R.string.error_password_required
		ValidationError.REQUIRED -> R.string.error_required
		ValidationError.INVALID -> R.string.error_invalid
	}

	return UiText.StringResourceId(res)
}