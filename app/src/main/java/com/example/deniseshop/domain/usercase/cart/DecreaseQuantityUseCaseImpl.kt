package com.example.deniseshop.domain.usercase.cart

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DecreaseQuantityUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): DecreaseQuantityUseCase {
	override fun invoke(id: Long): Flow<NetworkResponseState<String>> {
		return apiRepository.decreaseCartQuantity(id)
	}
}