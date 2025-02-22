package com.example.deniseshop.domain.usercase.search

import kotlinx.coroutines.flow.Flow

interface GetSearchHistoryUseCase {
	operator fun invoke(query: String): Flow<Set<String>>
}