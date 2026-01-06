package com.example.deniseshop.feature.editprofile.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.core.domain.model.User
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.usecase.GetUserUseCase
import com.example.deniseshop.core.domain.usecase.ValidateEmailUseCase
import com.example.deniseshop.core.domain.usecase.ValidateNameUseCase
import com.example.deniseshop.core.domain.usecase.ValidatePhoneUseCase
import com.example.deniseshop.core.presentation.toUiText
import com.example.deniseshop.feature.editprofile.domain.UpdateUserUserCase
import com.example.deniseshop.feature.editprofile.domain.UploadUserImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
	private val updateUser: UpdateUserUserCase,
	private val uploadUserImage: UploadUserImageUseCase,
	private val getUser: GetUserUseCase,
	private val validateName: ValidateNameUseCase,
	private val validateEmail: ValidateEmailUseCase,
	private val validatePhone: ValidatePhoneUseCase,
): ViewModel() {
	private val _state = MutableStateFlow(EditProfileState())

	val state = _state.asStateFlow()

	init {
		getProfileUser()
	}

	fun onEvent(event: EditProfileEvent){
		when(event) {
			is EditProfileEvent.EmailChange -> {
				_state.update { it.copy(email = event.email) }
			}
			is EditProfileEvent.FirstNameChange -> {
				_state.update { it.copy(firstName = event.fistName) }
			}
			is EditProfileEvent.ImageChange -> {
				_state.update {
					it.copy(imageUri = event.uri)
				}
				uploadImage(event.context, event.uri)
			}
			is EditProfileEvent.LastNameChange -> {
				_state.update { it.copy(lastName = event.lastName) }
			}
			is EditProfileEvent.PhoneChange -> {
				_state.update { it.copy(phone = event.phone) }
			}
			EditProfileEvent.ResetErrorSuccessState -> {
				_state.update { it.copy(error = null, success = false) }
			}
			EditProfileEvent.Update -> validate()
		}
	}

	private fun validate(){
		val firstNameResult = validateName(_state.value.firstName)
		val lastNameResult = validateName(_state.value.lastName)
		val emailResult = validateEmail(_state.value.email)
		val phoneResult = validatePhone(_state.value.phone)

		_state.update {
			it.copy(
				firstNameError = firstNameResult.error?.toUiText(),
				lastNameError = lastNameResult.error?.toUiText(),
				emailError = emailResult.error?.toUiText(),
				phoneError = phoneResult.error?.toUiText()
			)
		}

		if (
			firstNameResult.success &&
			lastNameResult.success &&
			emailResult.success &&
			phoneResult.success
		){
			updateProfileUser()
		}
	}

	private fun updateProfileUser(){
		viewModelScope.launch {
			_state.update { it.copy(isLoading = true) }

			updateUser(
				User(
					firstName = _state.value.firstName,
					lastName = _state.value.lastName,
					email = _state.value.email,
					phone = _state.value.phone
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

	private fun uploadImage(
		context: Context,
		uri: Uri
	){
		viewModelScope.launch {
			val stream = context.contentResolver.openInputStream(uri)

			val part = stream?.readBytes()?.toRequestBody("image/*".toMediaType())?.let {
				MultipartBody.Part.createFormData("image", "image.jpg", it)
			}

			part?.let {
				_state.update { it.copy(isUploading = true) }

				uploadUserImage(it)
					.onError { error ->
						_state.update {  state ->
							state.copy(
								isUploading = false,
								error = error.toUiText()
							)
						}
					}.onSuccess {
						_state.update {  state ->
							state.copy(
								isUploading = false,
								success = true
							)
						}
					}
			}
		}
	}

	private fun getProfileUser() {
		viewModelScope.launch {
			getUser().collect { user ->
				_state.update {
					it.copy(
						firstName = user?.firstName ?: "",
						lastName = user?.lastName ?: "",
						email = user?.email ?: "",
						phone = user?.phone ?: "",
						image = user?.image ?: ""
					)
				}
			}
		}
	}
}