package com.example.deniseshop.domain.usercase.cart

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddCartUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
):AddCartUseCase {
	override fun invoke(
		product: Long,
		quantity: Int?,
		size: String?,
		color: String?
	): Flow<NetworkResponseState<String>> {
		return apiRepository.addCart(product,quantity, size, color)
	}
}