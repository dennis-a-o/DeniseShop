package com.example.deniseshop.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.Home
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.core.domain.repository.UserSettingRepository
import com.example.deniseshop.core.presentation.ScreenState
import com.example.deniseshop.core.presentation.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val shopRepository: ShopRepository,
	private val settingRepository: UserSettingRepository
): ViewModel() {
	private val _state = MutableStateFlow<ScreenState<Home>>(ScreenState.Loading)

	val state = _state.asStateFlow()

	val cartItems = settingRepository.getCartItems()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList(),
		)

	val wishlistItems = settingRepository.getWishlistItems()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList(),
		)

	init {
		getHome()
	}

	fun refresh(){
		getHome()
	}

	fun onCartToggle(productId: Long){

	}

	fun onWishlistToggle(productId: Long){

	}

	private fun getHome(){
		viewModelScope.launch {
			_state.value = ScreenState.Loading

			shopRepository.getHome()
				.onSuccess { data ->
					_state.value = ScreenState.Success(data)
				}
				.onError { error ->
					_state.value = ScreenState.Error(error.toUiText())
				}
		}
	}
}