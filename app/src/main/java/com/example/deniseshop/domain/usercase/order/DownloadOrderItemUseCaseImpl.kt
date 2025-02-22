package com.example.deniseshop.domain.usercase.order

import com.example.deniseshop.common.state.DownloadState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DownloadOrderItemUseCaseImpl  @Inject constructor(
	private val apiRepository: ApiRepository
): DownloadOrderItemUseCase {
	override fun invoke(itemId: Long): Flow<DownloadState<String>> {
		return apiRepository.downloadOrderItem(itemId)
	}
}