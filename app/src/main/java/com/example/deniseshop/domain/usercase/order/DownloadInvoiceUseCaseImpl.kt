package com.example.deniseshop.domain.usercase.order

import com.example.deniseshop.common.state.DownloadState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DownloadInvoiceUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): DownloadInvoiceUseCase {
	override fun invoke(orderId: Long): Flow<DownloadState<String>> {
		return apiRepository.downloadOrderInvoice(orderId)
	}
}