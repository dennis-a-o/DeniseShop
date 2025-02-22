package com.example.deniseshop.domain.usercase.page

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiPage
import kotlinx.coroutines.flow.Flow

interface GetPageUseCase {
	operator fun invoke(name: String): Flow<NetworkResponseState<ApiPage>>
}