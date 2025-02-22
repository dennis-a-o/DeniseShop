package com.example.deniseshop.domain.usercase.order

import com.example.deniseshop.common.state.DownloadState
import kotlinx.coroutines.flow.Flow

interface DownloadOrderItemUseCase {
	operator fun invoke(itemId: Long): Flow<DownloadState<String>>
}