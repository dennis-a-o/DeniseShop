package com.example.deniseshop.domain.usercase.cart

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoveCartUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): RemoveCartUseCase {
	override fun invoke(product: Long): Flow<NetworkResponseState<String>> {
		return apiRepository.removeCart(product)
	}
}