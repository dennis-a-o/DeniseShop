package com.example.deniseshop.domain.usercase.search

import com.example.deniseshop.data.source.PreferencesDataSource
import javax.inject.Inject

class DeleteSearchHistoryUseCaseImpl @Inject constructor(
	private val preferencesDataSource: PreferencesDataSource
): DeleteSearchHistoryUseCase {
	override suspend fun invoke(query: String) {
		preferencesDataSource.deleteSearchQuery(query)
	}
}