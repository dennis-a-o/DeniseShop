package com.example.deniseshop.feature.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.Contact
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
class ContactViewModel @Inject constructor(
	private val shopRepository: ShopRepository
): ViewModel() {
	private val _state = MutableStateFlow<ScreenState<List<Contact>>>(ScreenState.Loading)

	val state = _state.asStateFlow()

	init {
		getContacts()
	}

	fun onRefresh(){
		getContacts()
	}

	private fun getContacts(){
		viewModelScope.launch {
			_state.value = ScreenState.Loading

			shopRepository.getContact()
				.onSuccess {
					_state.value = ScreenState.Success(it)
				}
				.onError {
					_state.value = ScreenState.Error(it.toUiText())
				}
		}
	}
}