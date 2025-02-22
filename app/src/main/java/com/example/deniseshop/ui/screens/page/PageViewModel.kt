package com.example.deniseshop.ui.screens.page

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.data.models.ApiPage
import com.example.deniseshop.domain.usercase.page.GetPageUseCase
import com.example.deniseshop.ui.mapper.BaseMapper
import com.example.deniseshop.ui.models.UiPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PageViewModel @Inject constructor(
	private val getPageUseCase: GetPageUseCase,
	private val pageApiToUiMapper: BaseMapper<ApiPage,UiPage>,
	savedStateHandle: SavedStateHandle
): ViewModel() {
	private val name:String = savedStateHandle["pageName"] ?: ""
	private val _pageState = MutableStateFlow<ScreenState<UiPage>>(ScreenState.Loading)
	private val _title = MutableStateFlow("")

	val pageState = _pageState.asStateFlow()
	val title = _title.asStateFlow()

	init {
		getPage()
	}

	fun onRetry(){
		getPage()
	}

	private fun getPage(){
		getPageUseCase(name).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					Log.e("PAGE_VM",it.exception.toString())
					_pageState.value = ScreenState.Error(it.exception.message.toString())
				}
				NetworkResponseState.Loading -> {
					_pageState.value = ScreenState.Loading
				}
				is NetworkResponseState.Success -> {
					_title.value = it.result.name
					_pageState.value = ScreenState.Success(pageApiToUiMapper.map(it.result))
				}
			}
		}.launchIn(viewModelScope)
	}
}