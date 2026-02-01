package com.example.deniseshop.feature.profile.domain

import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.repository.AuthRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
	private val authRepository: AuthRepository
) {
	suspend operator fun invoke(): Result<Unit, DataError.Remote>{
		return authRepository.deleteUser()
	}
}