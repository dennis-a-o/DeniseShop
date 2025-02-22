package com.example.deniseshop.domain.usercase.brand

import androidx.paging.PagingSource
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiBrand
import com.example.deniseshop.ui.models.UiBrand
import kotlinx.coroutines.flow.Flow

interface GetBrandUseCase {
	operator fun invoke(id: Long):Flow<NetworkResponseState<ApiBrand>>
	operator fun invoke(): PagingSource<Int, UiBrand>
}