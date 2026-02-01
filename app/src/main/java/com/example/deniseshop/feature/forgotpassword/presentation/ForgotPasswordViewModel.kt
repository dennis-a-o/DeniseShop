package com.example.deniseshop.feature.forgotpassword.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.usecase.ValidateEmailUseCase
import com.example.deniseshop.core.presentation.toUiText
import com.example.deniseshop.feature.forgotpassword.domain.ForgotPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
	private val validateEmail: ValidateEmailUseCase,
	private val forgotPassword: ForgotPasswordUseCase
): ViewModel() {
	private val _state = MutableStateFlow(ForgotPasswordState())

	val state = _state.asStateFlow()

	fun onEvent(event: ForgotPasswordEvent){
		when(event) {
			is ForgotPasswordEvent.EmailChange -> {
				_state.update{ it.copy(email = event.email) }
			}
			ForgotPasswordEvent.ForgotPassword -> validate()
			ForgotPasswordEvent.ResetState -> {
				_state.update{
					it.copy(success = false, error = null) }
			}
		}
	}

	private fun validate(){
		val emailResult = validateEmail(_state.value.email)

		_state.update {
			it.copy(
				emailError = emailResult.error?.toUiText()
			)
		}

		if (emailResult.success){
			forgotPassword()
		}
	}

	private  fun forgotPassword(){
		viewModelScope.launch {
			_state.update{ it.copy(isLoading = true) }

			forgotPassword(_state.value.email)
				.onError {error ->
					_state.update{
						it.copy(
							isLoading = false,
							error = error.toUiText()
						)
					}
				}
				.onSuccess {
					_state.update{
						it.copy(
							isLoading = false,
							success = true
						)
					}
				}
		}
	}
}