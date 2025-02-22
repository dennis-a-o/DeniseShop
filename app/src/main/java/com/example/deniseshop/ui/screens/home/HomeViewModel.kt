package com.example.deniseshop.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.data.models.ApiHome
import com.example.deniseshop.domain.usercase.home.GetHomeUseCase
import com.example.deniseshop.ui.mapper.BaseMapper
import com.example.deniseshop.ui.models.UiHome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val getHomeUseCase: GetHomeUseCase,
	private val homeApiToUiMapper: BaseMapper<ApiHome, UiHome>
):ViewModel() {
	private val _homeState: MutableStateFlow<ScreenState<UiHome>> = MutableStateFlow(value = ScreenState.Loading)
	private val _isRefreshing = MutableStateFlow(false)

	val homeState = _homeState.asStateFlow()
	val isRefreshing = _isRefreshing.asStateFlow()

	init {
		getHomeData()
	}

	fun onRefresh(){
		getFreshHomeData()
	}

	fun onRetry(){
		getHomeData()
	}

	private fun getHomeData(){
		getHomeUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_homeState.value = ScreenState.Error(it.exception.message.toString())
				}
				is NetworkResponseState.Loading -> {
					_homeState.value = ScreenState.Loading
				}
				is NetworkResponseState.Success -> {
					_homeState.value = ScreenState.Success(homeApiToUiMapper.map(it.result))
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun getFreshHomeData(){
		getHomeUseCase().onEach{
			when(it){
				is NetworkResponseState.Error -> {
					_isRefreshing.value = false
				}
				is NetworkResponseState.Loading -> {
					_isRefreshing.value = true
				}
				is NetworkResponseState.Success -> {
					_isRefreshing.value = false
					_homeState.value = ScreenState.Success(homeApiToUiMapper.map(it.result))
				}
			}
		}.launchIn(viewModelScope)
	}
}