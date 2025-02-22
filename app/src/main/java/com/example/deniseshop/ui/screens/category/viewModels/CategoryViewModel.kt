package com.example.deniseshop.ui.screens.category.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.domain.usercase.category.GetCategoryUseCase
import com.example.deniseshop.ui.mapper.CategoryListApiToUiMapper
import com.example.deniseshop.ui.models.UiCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
	private val getAllCategoryUseCase: GetCategoryUseCase,
	private val categoryListApiToUiMapper: CategoryListApiToUiMapper
): ViewModel() {
	private val _categoryState = MutableStateFlow<ScreenState<List<UiCategory>>>(value = ScreenState.Loading)

	val categoryState = _categoryState.asStateFlow()

	init {
		getAllCategories()
	}

	fun onRetry(){
		getAllCategories()
	}

	private fun getAllCategories(){
		getAllCategoryUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_categoryState.value = ScreenState.Error(it.exception.message.toString())
				}
				is NetworkResponseState.Loading -> {
					_categoryState.value = ScreenState.Loading
				}
				is NetworkResponseState.Success -> {
					_categoryState.value = ScreenState.Success(categoryListApiToUiMapper.map(it.result))
				}
			}
		}.launchIn(viewModelScope)
	}
}