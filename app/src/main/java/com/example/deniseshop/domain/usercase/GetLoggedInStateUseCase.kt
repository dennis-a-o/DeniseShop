package com.example.deniseshop.domain.usercase

import kotlinx.coroutines.flow.Flow

interface GetLoggedInStateUseCase {
	operator fun invoke(): Flow<Boolean>
}