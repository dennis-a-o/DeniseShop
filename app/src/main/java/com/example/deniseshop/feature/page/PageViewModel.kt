package com.example.deniseshop.feature.page

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.Page
import com.example.deniseshop.core.domain.model.PageType
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.core.presentation.ScreenState
import com.example.deniseshop.core.presentation.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageViewModel @Inject constructor(
	private val shopRepository: ShopRepository,
	savedStateHandle: SavedStateHandle,
): ViewModel() {
	private val page: PageType? = try {
		PageType.valueOf(savedStateHandle["page"] ?: "")
	}catch (e: Exception){
		null
	}
	private val _state = MutableStateFlow<ScreenState<Page>>(ScreenState.Loading)

	val state = _state.asStateFlow()

	init {
		page?.let {
			getPage(it)
		}
	}

	fun onRefresh(){
		page?.let {
			getPage(it)
		}
	}

	private fun  getPage(page: PageType){
		viewModelScope.launch {
			_state.value = ScreenState.Loading

				shopRepository.getPage(page)
					.onSuccess {
						_state.value = ScreenState.Success(it)
					}
					.onError {
						_state.value = ScreenState.Error(it.toUiText())
					}
		}
	}
}