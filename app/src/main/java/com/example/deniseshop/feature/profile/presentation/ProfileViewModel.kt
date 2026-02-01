package com.example.deniseshop.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.usecase.GetUserUseCase
import com.example.deniseshop.core.presentation.toUiText
import com.example.deniseshop.feature.profile.domain.DeleteAccountUseCase
import com.example.deniseshop.feature.profile.domain.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
	private val getUser: GetUserUseCase,
	private val logOut: LogOutUseCase,
	private val deleteAccount: DeleteAccountUseCase
): ViewModel() {
	private var getUserJob: Job? = null
	private val _state = MutableStateFlow(ProfileState())

	val state = _state.asStateFlow()

	init {
		getUserJob?.cancel()
		getUserJob = getProfileUser()
	}

	fun onEvent(event: ProfileEvent){
		when(event) {
			ProfileEvent.DeleteAccount -> deleteUserAccount()
			ProfileEvent.Logout -> logoutUser()
			ProfileEvent.Refresh -> refresh()
			ProfileEvent.ResetErrorState -> {
				_state.update { it.copy(error = null) }
			}
		}
	}


	private fun getProfileUser(): Job {
		return viewModelScope.launch {
			getUser().collect { user ->
				_state.update {
					it.copy(
						user = user,
						isRefreshing = false
					)
				}
			}
		}
	}

	private fun refresh(){
		_state.update { it.copy(isRefreshing = true) }

		getUserJob?.cancel()
		getUserJob = getProfileUser()
	}

	private fun logoutUser(){
		viewModelScope.launch {
			logOut()
				.onError { error ->
					_state.update { it.copy(error = error.toUiText()) }
				}.onSuccess {
					_state.update {
						it.copy(user = null)
					}
				}
		}
	}

	private fun deleteUserAccount(){
		viewModelScope.launch {
			deleteAccount()
				.onError { error ->
					_state.update { it.copy(error = error.toUiText()) }
				}.onSuccess {
					_state.update {
						it.copy(user = null)
					}
				}
		}
	}
}