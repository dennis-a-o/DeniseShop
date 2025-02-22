package com.example.deniseshop.domain.usercase.profile

import com.example.deniseshop.data.source.PreferencesDataSource
import com.example.deniseshop.ui.models.ThemeConfig
import javax.inject.Inject

class UpdateThemeUseCaseImpl @Inject constructor(
	private val dataStorePreferencesRepository: PreferencesDataSource
): UpdateThemeUseCase {
	override suspend fun invoke(themeConfig: ThemeConfig) {
			dataStorePreferencesRepository.setTheme(themeConfig)
	}
}