package com.example.deniseshop.feature.changetheme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.ThemeMode
import com.example.deniseshop.core.domain.repository.UserSettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeThemeViewModel @Inject constructor(
	private val userSettingRepository: UserSettingRepository
): ViewModel() {
	val themeMode = userSettingRepository.getThemeMode()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = ThemeMode.SYSTEM,
		)

	fun setThemeMode(themeMode: ThemeMode){
		viewModelScope.launch {
			userSettingRepository.setThemeMode(themeMode)
		}
	}
}