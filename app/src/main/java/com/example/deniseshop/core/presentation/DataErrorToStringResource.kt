package com.example.deniseshop.core.presentation

import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.DataError

fun DataError.toUiText(): UiText{
	val res = when(this) {
		DataError.Remote.REQUEST_TIMEOUT -> R.string.error_rquest_timeout
		DataError.Remote.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
		DataError.Remote.NO_INTERNET -> R.string.error_no_internet
		DataError.Remote.SERVER -> R.string.error_unknown
		DataError.Remote.SERIALIZATION -> R.string.error_serialization
		DataError.Remote.UNKNOWN -> R.string.error_unknown
		is DataError.RemoteFormError -> R.string.error_invalid_input
	}

	return UiText.StringResourceId(res)
}