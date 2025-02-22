package com.example.deniseshop.domain.usercase.cart

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiCart
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetCartUseCase {
	override fun invoke(): Flow<NetworkResponseState<ApiCart>> {
		return apiRepository.getCart()
	}
}