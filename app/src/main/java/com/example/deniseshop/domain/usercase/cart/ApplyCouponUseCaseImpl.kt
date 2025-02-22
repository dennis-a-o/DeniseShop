package com.example.deniseshop.domain.usercase.cart

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApplyCouponUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): ApplyCouponUseCase {
	override fun invoke(coupon: String): Flow<NetworkResponseState<String>> {
		return apiRepository.applyCoupon(coupon)
	}
}