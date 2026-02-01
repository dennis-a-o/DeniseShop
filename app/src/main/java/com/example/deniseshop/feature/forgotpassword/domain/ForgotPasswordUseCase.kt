package com.example.deniseshop.feature.forgotpassword.domain

import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
	private val authRepository: AuthRepository
) {
	suspend  operator fun invoke(email: String): Result<Unit, DataError> {
		return authRepository.forgotPassword(email)
	}
}