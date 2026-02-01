package com.example.deniseshop.feature.signup.domain

import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.UserSignUp
import com.example.deniseshop.core.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
	private val authRepository: AuthRepository
){
	suspend operator fun invoke(
		user: UserSignUp
	): Result<Unit, DataError>{
		return authRepository.signUp(user)
	}
}