package com.example.deniseshop.core.domain.model

sealed interface DataError: Error {
	enum class Remote: DataError {
		REQUEST_TIMEOUT,
		TOO_MANY_REQUESTS,
		NO_INTERNET,
		SERVER,
		SERIALIZATION,
		UNKNOWN
	}
	data class RemoteFormError(val error: String): DataError
}