package com.example.deniseshop.domain.usercase.order

import com.example.deniseshop.common.state.DownloadState
import kotlinx.coroutines.flow.Flow

interface DownloadInvoiceUseCase {
	operator fun invoke(orderId: Long): Flow<DownloadState<String>>
}