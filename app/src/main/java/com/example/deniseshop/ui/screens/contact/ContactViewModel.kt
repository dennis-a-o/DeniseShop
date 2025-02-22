package com.example.deniseshop.ui.screens.contact

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.data.models.ApiContact
import com.example.deniseshop.domain.usercase.contact.GetContactUseCase
import com.example.deniseshop.ui.mapper.BaseListMapper
import com.example.deniseshop.ui.models.UiContact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
	private val getContactUseCase: GetContactUseCase,
	private val contactListApiToUiMapper: BaseListMapper<ApiContact, UiContact>
): ViewModel() {
	private val _contactState = MutableStateFlow<ScreenState<List<UiContact>>>(ScreenState.Loading)

	val contactState = _contactState.asStateFlow()

	init {
		getContact()
	}

	fun onRetry(){
		getContact()
	}

	private fun getContact(){
		getContactUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					Log.e("CONTACT_VM",it.exception.toString())
					_contactState.value = ScreenState.Error(it.exception.message.toString())
				}
				NetworkResponseState.Loading -> ScreenState.Loading
				is NetworkResponseState.Success -> {
					_contactState.value = ScreenState.Success(contactListApiToUiMapper.map(it.result))
				}
			}
		}.launchIn(viewModelScope)
	}
}