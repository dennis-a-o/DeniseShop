package com.example.deniseshop.domain.usercase.search

interface DeleteSearchHistoryUseCase {
	suspend operator fun  invoke(query: String)
}