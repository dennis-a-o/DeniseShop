package com.example.deniseshop.domain.usercase.checkout

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaypalPaymentSuccessUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
):PaypalPaymentSuccessUseCase {
	override fun invoke(token: String, payerId: String): Flow<NetworkResponseState<String>> {
		return apiRepository.paypalPaymentSuccess(token, payerId)
	}
}