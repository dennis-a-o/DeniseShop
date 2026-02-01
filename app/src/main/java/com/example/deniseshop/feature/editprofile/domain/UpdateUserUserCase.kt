package com.example.deniseshop.feature.editprofile.domain

import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.User
import com.example.deniseshop.core.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateUserUserCase @Inject constructor(
	private val authRepository: AuthRepository
) {
	suspend operator fun invoke(
		user:  User
	): Result<Unit, DataError>{
		return authRepository.updateUser(user)
	}
}