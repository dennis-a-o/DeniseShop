package com.example.deniseshop.domain.usercase.cart

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClearCouponUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): ClearCouponUseCase {
	override fun invoke(): Flow<NetworkResponseState<String>> {
		return apiRepository.clearCoupon()
	}
}