package com.example.deniseshop.feature.changepassword.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.usecase.ValidatePasswordUseCase
import com.example.deniseshop.core.presentation.toUiText
import com.example.deniseshop.feature.changepassword.domain.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
	private val changePassword: ChangePasswordUseCase,
	private val validatePassword: ValidatePasswordUseCase
): ViewModel() {
	private val _state = MutableStateFlow(ChangePasswordState())

	val state = _state.asStateFlow()

	fun onEvent(event: ChangePasswordEvent){
		when(event) {
			ChangePasswordEvent.ChangePassword -> validate()
			is ChangePasswordEvent.CurrentPasswordChange -> {
				_state.update { it.copy(currentPassword = event.password) }
			}
			is ChangePasswordEvent.NewPasswordChange -> {
				_state.update { it.copy(newPassword = event.password) }
			}
			ChangePasswordEvent.ResetErrorSuccessState -> {
				_state.update { it.copy(error = null, success = false) }
			}
		}
	}

	private fun validate(){
		val currentPasswordResult = validatePassword(_state.value.currentPassword)
		val newPasswordResult = validatePassword(_state.value.newPassword)

		_state.update {
			it.copy(
				currentPasswordError = currentPasswordResult.error?.toUiText(),
				newPasswordError = newPasswordResult.error?.toUiText()
			)
		}

		if (currentPasswordResult.success && newPasswordResult.success){
			updatePassword()
		}
	}

	private fun updatePassword(){
		viewModelScope.launch {
			_state.update { it.copy(isLoading = true) }

			changePassword(
				currentPassword = _state.value.currentPassword,
				newPassword = _state.value.newPassword
			).onError { error ->
				_state.update {
					it.copy(
						isLoading = false,
						error = error.toUiText()
					)
				}
			}.onSuccess {
				_state.update {
					it.copy(
						isLoading = false,
						success = true
					)
				}
			}
		}
	}
}