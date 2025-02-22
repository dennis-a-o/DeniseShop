package com.example.deniseshop.domain.usercase

import com.example.deniseshop.data.source.PreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoggedInStateUseCaseImpl @Inject constructor(
	private val preferencesDataSource: PreferencesDataSource
): GetLoggedInStateUseCase {
	override fun invoke(): Flow<Boolean> {
		return preferencesDataSource.isLoggedIn()
	}
}