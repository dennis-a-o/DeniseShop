package com.example.deniseshop.domain.usercase.search

import com.example.deniseshop.data.source.PreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchHistoryUseCaseImpl @Inject constructor(
	private val  preferencesDataSource: PreferencesDataSource
): GetSearchHistoryUseCase {
	override fun invoke(query: String): Flow<Set<String>> {
		return preferencesDataSource.getSearchQueryHistory(query)
	}
}