package com.example.deniseshop.feature.signin.domain

import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
	private val authRepository: AuthRepository
) {
	suspend operator fun invoke(
		email: String,
		password: String
	): Result<Unit, DataError>{
		return authRepository.signIn(
			email = email,
			password = password
		)
	}
}