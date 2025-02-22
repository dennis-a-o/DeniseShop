package com.example.deniseshop.ui.screens.auth.viewModels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.domain.models.UserSignUpData
import com.example.deniseshop.domain.usercase.ValidateInputUseCase
import com.example.deniseshop.domain.usercase.auth.SignUpUseCase
import com.example.deniseshop.ui.models.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
	private val signUpUseCase: SignUpUseCase,
	private val validateInputUseCase: ValidateInputUseCase,
	@ApplicationContext val context: Context
): ViewModel() {
	private val _signUpState = mutableStateOf(SignUpState())
	private val _signUpMessage = MutableStateFlow("")
	private val _signUpSuccess = MutableStateFlow(false)
	private val _signUpError = MutableStateFlow(false)


	val signUpState = _signUpState
	val signUpMessage = _signUpMessage.asStateFlow()
	val signUpSuccess = _signUpSuccess.asStateFlow()
	val signUpError = _signUpError.asStateFlow()



	fun onEvent(signUpEvent: SignUpEvent){
		when(signUpEvent){
			is SignUpEvent.FirstNameChanged -> {
				_signUpState.value = _signUpState.value.copy(firstName = signUpEvent.firstName)
			}
			is  SignUpEvent.LastNameChanged -> {
				_signUpState.value = _signUpState.value.copy(lastName = signUpEvent.lastName)
			}
			is  SignUpEvent.EmailChanged -> {
				_signUpState.value = _signUpState.value.copy(email = signUpEvent.email)
			}
			is  SignUpEvent.PhoneChanged -> {
				_signUpState.value = _signUpState.value.copy(phone = signUpEvent.phone)
			}
			is  SignUpEvent.PasswordChanged -> {
				_signUpState.value = _signUpState.value.copy(password = signUpEvent.password)
			}
			is  SignUpEvent.VisiblePassword -> {
				_signUpState.value = _signUpState.value.copy( isVisiblePassword = signUpEvent.visible)
			}
			is  SignUpEvent.AcceptTermChanged-> {
				_signUpState.value = _signUpState.value.copy(acceptTerms = signUpEvent.accept)
			}
			else -> validateSignUp()
		}
	}

	fun signUpErrorReset(){
		_signUpError.value = false
		_signUpMessage.value = ""
	}

	fun signUpSuccessReset(){
		_signUpSuccess.value = false
		_signUpMessage.value = ""
	}

	private fun validateSignUp(){
		val firstNameResult = validateInputUseCase.validateName(_signUpState.value.firstName)
		val lastNameResult = validateInputUseCase.validateName(_signUpState.value.lastName)
		val emailResult = validateInputUseCase.validateEmail(_signUpState.value.email)
		val phoneResult = validateInputUseCase.validatePhone(_signUpState.value.phone)
		val passwordResult = validateInputUseCase.validatePassword(_signUpState.value.password)
		val acceptsTermResult = validateInputUseCase.validateAcceptTerms(_signUpState.value.acceptTerms)

		_signUpState.value = _signUpState.value.copy(
			firstNameError = firstNameResult.errorMessage,
			lastNameError = lastNameResult.errorMessage,
			emailError = emailResult.errorMessage,
			phoneError = phoneResult.errorMessage,
			passwordError = passwordResult.errorMessage,
			acceptTermsError =  acceptsTermResult.errorMessage
		)

		if (firstNameResult.successful &&
			lastNameResult.successful &&
			emailResult.successful &&
			phoneResult.successful &&
			passwordResult.successful  &&
			acceptsTermResult.successful
	   ){
			 signUpUseCase.invoke(
				UserSignUpData(
					firstName = _signUpState.value.firstName,
					lastName = _signUpState.value.lastName,
					phone = _signUpState.value.phone,
					email = _signUpState.value.email,
					password = _signUpState.value.password,
					acceptTerms = _signUpState.value.acceptTerms
				)
			).onEach {
				when(it){
					is NetworkResponseState.Loading -> {
						_signUpState.value = _signUpState.value.copy(isLoading = true)
					}
					is NetworkResponseState.Error -> {
						_signUpState.value = _signUpState.value.copy(isLoading = false)
						_signUpError.value = true
						_signUpMessage.value = it.exception.message.toString()
					}
					is NetworkResponseState.Success -> {
						_signUpState.value = _signUpState.value.copy(isLoading = false)
						_signUpMessage.value = it.result
						_signUpSuccess.value = true
					}
				}
			}.launchIn(viewModelScope)
		}
	}
}