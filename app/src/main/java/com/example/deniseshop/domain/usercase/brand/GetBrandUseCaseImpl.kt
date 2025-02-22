package com.example.deniseshop.domain.usercase.brand

import androidx.paging.PagingSource
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiBrand
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.ui.models.UiBrand
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBrandUseCaseImpl @Inject constructor(private val apiRepository: ApiRepository): GetBrandUseCase {
	override fun invoke(id: Long): Flow<NetworkResponseState<ApiBrand>> {
		return apiRepository.getBrand(id)
	}

	override fun invoke(): PagingSource<Int, UiBrand> {
		return apiRepository.getAllBrand()
	}
}