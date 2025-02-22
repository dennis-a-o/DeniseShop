package com.example.deniseshop.domain.usercase.brand

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiBrand
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryBrandsUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetCategoryBrandsUseCase {
	override fun invoke(category: Long): Flow<NetworkResponseState<List<ApiBrand>>> {
		return apiRepository.getBrandByCategory(category)
	}
}