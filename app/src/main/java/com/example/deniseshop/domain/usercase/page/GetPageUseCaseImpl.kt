package com.example.deniseshop.domain.usercase.page

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiPage
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPageUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetPageUseCase {
	override fun invoke(name: String): Flow<NetworkResponseState<ApiPage>> {
		return apiRepository.getPage(name)
	}
}