package com.example.deniseshop.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.data.repository.PreferenceRepository
import com.example.deniseshop.data.source.PreferencesDataSource
import com.example.deniseshop.ui.models.UiSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
	preferenceRepository: PreferenceRepository
): ViewModel() {
	val uiState = preferenceRepository.getSetting().map {
		MainActivityUiState.Success(it)
	}.stateIn(
		scope = viewModelScope,
		initialValue = MainActivityUiState.Loading,
		started = SharingStarted.WhileSubscribed(5_000)
	)

}

sealed interface MainActivityUiState {
	data object Loading : MainActivityUiState
	data class Success(val uiSetting: UiSetting) : MainActivityUiState
}