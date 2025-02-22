package com.example.deniseshop.ui.screens.auth.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.domain.models.UserSignInData
import com.example.deniseshop.domain.usercase.ValidateInputUseCase
import com.example.deniseshop.domain.usercase.auth.SignInUseCase
import com.example.deniseshop.ui.models.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
	private val singInUseCase: SignInUseCase,
	private val validateInputUseCase: ValidateInputUseCase
):ViewModel() {
	private val _signInState = mutableStateOf(SignInState())
	private val _signInMessage = MutableStateFlow("")
	private val _signInSuccess = MutableStateFlow(false)
	private val _signInError = MutableStateFlow(false)


	val signInState = _signInState
	val signInMessage = _signInMessage.asStateFlow()
	val signInSuccess = _signInSuccess.asStateFlow()
	val signInError = _signInError.asStateFlow()

	fun onEvent(signInEvent: SignInEvent){
		when(signInEvent){
			is SignInEvent.EmailChanged -> {
				_signInState.value = _signInState.value.copy(email = signInEvent.email)
			}
			is SignInEvent.PasswordChanged -> {
				_signInState.value = _signInState.value.copy(password =  signInEvent.password)
			}
			is SignInEvent.VisiblePassword -> {
				_signInState.value = _signInState.value.copy(isVisiblePassword = signInEvent.visible)
			}
			is SignInEvent.Submit -> validateSignIn()
		}
	}

	fun signInErrorReset(){
		_signInError.value = false
		_signInMessage.value = ""
	}

	fun signInSuccessReset(){
		_signInSuccess.value = false
		_signInMessage.value = ""
	}

	private  fun  validateSignIn(){
		val emailResult = validateInputUseCase.validateEmail(_signInState.value.email)
		val passwordResult = validateInputUseCase.validatePassword(_signInState.value.password)

		_signInState.value = _signInState.value.copy(
			emailError = emailResult.errorMessage,
			passwordError = passwordResult.errorMessage
		)

		if (emailResult.successful && passwordResult.successful){
			singInUseCase.invoke(
				email = _signInState.value.email,
				password = _signInState.value.password
			).onEach {
				when(it){
					is NetworkResponseState.Loading -> {
						_signInState.value = _signInState.value.copy(isLoading = true)
					}
					is NetworkResponseState.Error -> {
						_signInState.value = _signInState.value.copy(isLoading = false)
						_signInError.value = true
						_signInMessage.value = it.exception.message.toString()
					}
					is NetworkResponseState.Success -> {
						_signInState.value = _signInState.value.copy(isLoading = false)
						_signInSuccess.value = true
						_signInMessage.value = it.result
					}
				}
			}.launchIn(viewModelScope)
		}
	}
}