package com.example.deniseshop.domain.usercase.profile

import com.example.deniseshop.ui.models.ThemeConfig

interface UpdateThemeUseCase {
	suspend operator fun invoke(themeConfig: ThemeConfig)
}