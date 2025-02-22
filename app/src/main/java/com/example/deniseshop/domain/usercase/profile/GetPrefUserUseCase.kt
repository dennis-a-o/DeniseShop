package com.example.deniseshop.domain.usercase.profile

import com.example.deniseshop.domain.models.PrefUser
import kotlinx.coroutines.flow.Flow

interface GetPrefUserUseCase {
	 operator fun invoke(): Flow<PrefUser>
}