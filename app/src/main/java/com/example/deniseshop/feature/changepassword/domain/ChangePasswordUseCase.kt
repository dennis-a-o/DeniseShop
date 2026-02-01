package com.example.deniseshop.feature.changepassword.domain

import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.repository.AuthRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
	private val authRepository: AuthRepository
) {
	suspend operator fun invoke(
		currentPassword: String,
		newPassword: String
	): Result<Unit, DataError>{
		return authRepository.changePassword(
			currentPassword = currentPassword,
			newPassword = newPassword
		)
	}
}