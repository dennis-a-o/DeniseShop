package com.example.deniseshop.feature.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.Page
import com.example.deniseshop.core.domain.model.PageType
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.core.presentation.ScreenState
import com.example.deniseshop.core.presentation.toUiText
import com.example.deniseshop.navigation.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PageViewModel.Factory::class)
class PageViewModel @AssistedInject constructor(
	private val shopRepository: ShopRepository,
	@Assisted val navKey: Route.Page
): ViewModel() {
	private val page: PageType = navKey.pageType
	private val _state = MutableStateFlow<ScreenState<Page>>(ScreenState.Loading)

	val state = _state.asStateFlow()

	init {
		getPage(page)
	}

	fun onRefresh(){
		getPage(page)
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

	@AssistedFactory
	interface Factory {
		fun create(navKey: Route.Page): PageViewModel
	}
}