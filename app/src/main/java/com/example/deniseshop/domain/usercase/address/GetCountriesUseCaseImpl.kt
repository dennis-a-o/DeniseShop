package com.example.deniseshop.domain.usercase.address

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCountriesUseCaseImpl @Inject constructor(private val apiRepository: ApiRepository):GetCountriesUseCase {
	override fun invoke(): Flow<NetworkResponseState<List<String>>> {
		return apiRepository.getCountries()
	}
}