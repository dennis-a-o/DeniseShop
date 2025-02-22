package com.example.deniseshop.domain.usercase.checkout

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreatePaypalPaymentUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
):CreatePaypalPaymentUseCase {
	override fun invoke(): Flow<NetworkResponseState<String>> {
		return apiRepository.createPaypalPayment()
	}
}