package com.example.deniseshop.feature.signup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.UserSignUp
import com.example.deniseshop.core.domain.model.ValidationError
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.usecase.ValidateEmailUseCase
import com.example.deniseshop.core.domain.usecase.ValidateNameUseCase
import com.example.deniseshop.core.domain.usecase.ValidatePasswordUseCase
import com.example.deniseshop.core.domain.usecase.ValidatePhoneUseCase
import com.example.deniseshop.core.presentation.toUiText
import com.example.deniseshop.feature.signup.domain.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
	private val validateName: ValidateNameUseCase,
	private val validateEmail: ValidateEmailUseCase,
	private val validatePassword: ValidatePasswordUseCase,
	private val validatePhone: ValidatePhoneUseCase,
	private val signUp: SignUpUseCase
): ViewModel() {
	private val _state = MutableStateFlow(SignUpState())

	val state = _state.asStateFlow()

	fun onEvent(event: SignUpEvent){
		when(event) {
			is SignUpEvent.AcceptTermChange -> {
				_state.update { it.copy(acceptTerms = event.accept) }
			}
			is SignUpEvent.EmailChange -> {
				_state.update { it.copy(email = event.email) }
			}
			is SignUpEvent.FirstNameChange -> {
				_state.update { it.copy(firstName = event.firstName) }
			}
			is SignUpEvent.LastNameChange -> {
				_state.update { it.copy(lastName = event.lastName) }
			}
			is SignUpEvent.PasswordChange -> {
				_state.update { it.copy(password = event.password) }
			}
			is SignUpEvent.PhoneChange -> {
				_state.update { it.copy(phone = event.phone) }
			}
			SignUpEvent.ResetState -> {
				_state.update { it.copy(error = null, success = false) }
			}
			SignUpEvent.SignUp -> validate()
		}
	}

	private fun validate(){
		val firstNameResult = validateName(_state.value.firstName)
		val lastNameResult = validateName(_state.value.lastName)
		val emailResult = validateEmail(_state.value.email)
		val phoneResult = validatePhone(_state.value.phone)
		val passwordResult = validatePassword(_state.value.password)
		val acceptedTerms = validateAcceptTerms(_state.value.acceptTerms)

		_state.update {
			it.copy(
				firstNameError = firstNameResult.error?.toUiText(),
				lastNameError = lastNameResult.error?.toUiText(),
				emailError = emailResult.error?.toUiText(),
				phoneError = phoneResult.error?.toUiText(),
				passwordError = passwordResult.error?.toUiText(),
				acceptTermsError = acceptedTerms?.toUiText(),
			)
		}

		if (
			firstNameResult.success &&
			lastNameResult.success &&
			emailResult.success &&
			phoneResult.success &&
			passwordResult.success &&
			acceptedTerms == null
		){
			signUpUser()
		}
	}

	private fun signUpUser(){
		viewModelScope.launch {
			_state.update { it.copy(isLoading = true) }

			signUp(
				UserSignUp(
					firstName = _state.value.firstName,
					lastName = _state.value.lastName,
					email = _state.value.email,
					phone = _state.value.phone,
					password = _state.value.password,
					acceptTerms = _state.value.acceptTerms
				)
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

	private fun validateAcceptTerms(accepted: Boolean): ValidationError? {
		return if (accepted){
			null
		}else{
			ValidationError.ACCEPT_TERMS
		}
	}
}