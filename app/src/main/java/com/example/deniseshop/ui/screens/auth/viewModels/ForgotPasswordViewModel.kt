package com.example.deniseshop.ui.screens.auth.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.domain.usercase.ValidateInputUseCase
import com.example.deniseshop.domain.usercase.auth.ForgotPasswordUseCase
import com.example.deniseshop.ui.models.ForgotPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
	private val forgotPasswordUseCase: ForgotPasswordUseCase,
	private val validateInputUseCase: ValidateInputUseCase
): ViewModel() {
	private val _forgotPasswordState = mutableStateOf(ForgotPasswordState())
	private val _forgotPasswordMessage = MutableStateFlow("")
	private val _forgotPasswordSuccess = MutableStateFlow(false)
	private val _forgotPasswordError = MutableStateFlow(false)

	val forgotPasswordMessage = _forgotPasswordMessage.asStateFlow()
	val forgotPasswordSuccess = _forgotPasswordSuccess.asStateFlow()
	val forgotPasswordError = _forgotPasswordError.asStateFlow()
	val forgotPasswordState = _forgotPasswordState

	fun onEvent(event: ForgotPasswordEvent){
		when(event){
			is ForgotPasswordEvent.EmailChanged ->{
				_forgotPasswordState.value = _forgotPasswordState.value.copy(email = event.email)
			}
			is ForgotPasswordEvent.Submit -> validate()
		}
	}

	fun forgotPasswordErrorReset(){
		_forgotPasswordError.value = false
		_forgotPasswordMessage.value = ""
	}

	fun forgotPasswordSuccessReset(){
		_forgotPasswordSuccess.value = false
		_forgotPasswordMessage.value = ""
	}

	private fun validate(){
		val emailResult = validateInputUseCase.validateEmail(_forgotPasswordState.value.email)

		_forgotPasswordState.value = _forgotPasswordState.value.copy(
			emailError = emailResult.errorMessage,
		)
		if (emailResult.successful){
			forgotPasswordUseCase.invoke(_forgotPasswordState.value.email)
			.onEach {
				when(it){
					is NetworkResponseState.Loading -> {
						_forgotPasswordState.value = _forgotPasswordState.value.copy(isLoading = true)
					}
					is NetworkResponseState.Error -> {
						_forgotPasswordState.value = _forgotPasswordState.value.copy(isLoading = false)
						_forgotPasswordError.value = true
						_forgotPasswordMessage.value = it.exception.message.toString()
					}
					is NetworkResponseState.Success -> {
						_forgotPasswordState.value = _forgotPasswordState.value.copy(isLoading = false)
						_forgotPasswordSuccess.value = true
						_forgotPasswordMessage.value = it.result
					}
				}
			}.launchIn(viewModelScope)
		}
	}
}