package com.example.deniseshop.core.domain.usecase

import com.example.deniseshop.core.domain.model.User
import com.example.deniseshop.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
	private val authRepository: AuthRepository
) {
	operator fun invoke(): Flow<User?>{
		return authRepository.getUser()
	}
}