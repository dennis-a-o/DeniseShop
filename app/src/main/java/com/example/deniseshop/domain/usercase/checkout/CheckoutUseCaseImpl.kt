package com.example.deniseshop.domain.usercase.checkout

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiCheckout
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckoutUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
):CheckoutUseCase {
	override fun invoke(): Flow<NetworkResponseState<ApiCheckout>> {
		return apiRepository.checkout()
	}
}