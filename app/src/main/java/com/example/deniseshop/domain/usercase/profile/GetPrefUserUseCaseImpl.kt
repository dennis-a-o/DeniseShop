package com.example.deniseshop.domain.usercase.profile

import com.example.deniseshop.data.repository.PreferenceRepository
import com.example.deniseshop.domain.models.PrefUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPrefUserUseCaseImpl @Inject constructor(
	private val preferenceRepository: PreferenceRepository
):GetPrefUserUseCase {
	override fun invoke(): Flow<PrefUser>  = preferenceRepository.getPrefUser()
}