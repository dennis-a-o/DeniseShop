package com.example.deniseshop.domain.usercase.category

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiCategory
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetCategoryUseCase {
	override fun invoke(): Flow<NetworkResponseState<List<ApiCategory>>> {
		return apiRepository.getCategories()
	}

	override fun invoke(id: Long): Flow<NetworkResponseState<ApiCategory>> {
		return apiRepository.getCategory(id)
	}
}