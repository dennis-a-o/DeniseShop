package com.example.deniseshop.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.ThemeMode
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.core.domain.repository.UserSettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
	private val settingRepository: UserSettingRepository,
	private val shopRepository: ShopRepository,
): ViewModel() {
	val uiState = settingRepository.getThemeMode().map {
		MainActivityUiState.Success(it)
	}.stateIn(
		scope = viewModelScope,
		initialValue = MainActivityUiState.Loading,
		started = SharingStarted.WhileSubscribed(5_000)
	)

	fun sync(){
		viewModelScope.launch {
			shopRepository.syncWishlistItems()
			shopRepository.syncCartItems()
		}
	}

}

sealed interface MainActivityUiState {
	data object Loading : MainActivityUiState
	data class Success(val themeMode: ThemeMode) : MainActivityUiState
}