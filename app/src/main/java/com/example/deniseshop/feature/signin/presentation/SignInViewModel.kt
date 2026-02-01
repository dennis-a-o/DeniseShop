package com.example.deniseshop.feature.signin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.usecase.ValidateEmailUseCase
import com.example.deniseshop.core.domain.usecase.ValidatePasswordUseCase
import com.example.deniseshop.core.presentation.toUiText
import com.example.deniseshop.feature.signin.domain.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
	private val signIn: SignInUseCase,
	private val validateEmail: ValidateEmailUseCase,
	private val validatePassword: ValidatePasswordUseCase
): ViewModel() {
	private val _state = MutableStateFlow(SignInState())

	val state = _state.asStateFlow()

	fun onEvent(event: SignInEvent){
		when(event) {
			is SignInEvent.EmailChange -> {
				_state.update { it.copy(email = event.email) }
			}
			is SignInEvent.PasswordChange -> {
				_state.update { it.copy(password = event.password) }
			}
			SignInEvent.ResetState -> {
				_state.update { it.copy(error = null, success = false) }
			}
			SignInEvent.SignIn -> validate()
		}
	}

	private fun validate(){
		val emailResult = validateEmail(_state.value.email)
		val passwordResult = validatePassword(_state.value.password)

		_state.update {
			it.copy(
				emailError = emailResult.error?.toUiText(),
				passwordError = passwordResult.error?.toUiText()
			)
		}

		if (emailResult.success && passwordResult.success){
			signInUser()
		}
	}

	private fun signInUser(){
		viewModelScope.launch {
			_state.update { it.copy(isLoading = true) }

			signIn(
				email = _state.value.email,
				password = _state.value.password
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