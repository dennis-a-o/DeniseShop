package com.example.deniseshop.feature.orderdetail

import com.example.deniseshop.core.domain.model.OrderDetail
import com.example.deniseshop.core.presentation.UiText

data class OrderDetailState(
	val orderDetail: OrderDetail? = null,
	val isLoading: Boolean = false,
	val loadingError: UiText? = null,
	val isSubmittingReview: Boolean = false,
	val isDownloadingItem: Boolean = false,
	val isDownloadingInvoice: Boolean = false,
	val error: UiText? = null,
	val success: String? = null,
)