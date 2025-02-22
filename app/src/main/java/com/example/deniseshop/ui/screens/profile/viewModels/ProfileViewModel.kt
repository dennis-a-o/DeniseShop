package com.example.deniseshop.ui.screens.profile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiUser
import com.example.deniseshop.data.repository.PreferenceRepository
import com.example.deniseshop.domain.models.PrefUser
import com.example.deniseshop.ui.models.ThemeConfig
import com.example.deniseshop.domain.models.UpdateProfileData
import com.example.deniseshop.domain.usercase.GetLoggedInStateUseCase
import com.example.deniseshop.domain.usercase.ValidateInputUseCase
import com.example.deniseshop.domain.usercase.profile.ChangePasswordUseCase
import com.example.deniseshop.domain.usercase.profile.DeleteAccountUseCase
import com.example.deniseshop.domain.usercase.profile.GetApiUserUseCase
import com.example.deniseshop.domain.usercase.profile.GetPrefUserUseCase
import com.example.deniseshop.domain.usercase.profile.LogoutUseCase
import com.example.deniseshop.domain.usercase.profile.UpdateUserUseCase
import com.example.deniseshop.domain.usercase.profile.UpdateThemeUseCase
import com.example.deniseshop.ui.mapper.BaseMapper
import com.example.deniseshop.ui.models.ChangePasswordState
import com.example.deniseshop.ui.models.EditUserState
import com.example.deniseshop.ui.models.ProfileState
import com.example.deniseshop.ui.models.UiSetting
import com.example.deniseshop.ui.models.UiUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
	private val getApiUserUseCase: GetApiUserUseCase,
	private val getLoggedInStateUseCase: GetLoggedInStateUseCase,
	private val getPrefUserUseCase: GetPrefUserUseCase,
	private val validateInputUseCase: ValidateInputUseCase,
	private val updateThemeUseCase: UpdateThemeUseCase,
	private val updateUserUseCase: UpdateUserUseCase,
	private val changePasswordUseCase: ChangePasswordUseCase,
	private val logoutUseCase: LogoutUseCase,
	private val deleteAccountUseCase: DeleteAccountUseCase,
	private val userApiToUiMapper: BaseMapper<ApiUser, UiUser>,
	private val userPrefToUiMapper: BaseMapper<PrefUser, UiUser>,
	private val preferenceRepository: PreferenceRepository
): ViewModel() {
	private val _profileState = MutableStateFlow(ProfileState())
	private val _uiUser = MutableStateFlow(UiUser())
	private val _uiSetting = MutableStateFlow(UiSetting())
	private val _editUserState = MutableStateFlow(EditUserState())
	private val _changePasswordState = MutableStateFlow(ChangePasswordState())


	val profileState = _profileState.asStateFlow()
	val uiUser = _uiUser.asStateFlow()
	val uiSetting = _uiSetting.asStateFlow()
	val editUserState = _editUserState.asStateFlow()
	val changePasswordState = _changePasswordState.asStateFlow()

	init {
		checkLoggedIn()
		getPrefUser()
		getSetting()
	}

	fun onProfileEvent(event: ProfileEvent){
		when(event){
			is ProfileEvent.ChangePassword -> {
				_profileState.value = _profileState.value.copy(changePassword = event.change)
			}
			is ProfileEvent.DeleteProfile ->{
				deleteAccount()
			}
			is ProfileEvent.EditUser -> {
				_profileState.value = _profileState.value.copy(
					editUser = event.edit
				)
				if (event.edit){
					_editUserState.value = _editUserState.value.copy(
						firstName = _uiUser.value.firstName,
						lastName = _uiUser.value.lastName,
						phone = _uiUser.value.phone,
						email = _uiUser.value.email,
					)
				}
			}
			ProfileEvent.Logout -> {
				logout()
			}
			is ProfileEvent.ChangeTheme -> {
				_profileState.value = _profileState.value.copy(changeTheme = event.change)
			}

			ProfileEvent.Reset -> {
				_profileState.value = ProfileState(isLoggedIn = _profileState.value.isLoggedIn)
			}
		}
	}

	fun onEditUserEvent(event: EditUserEvent){
		when(event){
			is EditUserEvent.EmailChanged -> {
				_editUserState.value = _editUserState.value.copy(email = event.email)
			}
			is EditUserEvent.FirstNameChanged -> {
				_editUserState.value = _editUserState.value.copy(firstName = event.fistName)
			}
			is EditUserEvent.ImageChanged -> {
				_editUserState.value = _editUserState.value.copy(imageUri = event.imageUri)
				uploadProfileImage()
			}
			is EditUserEvent.LastNameChanged -> {
				_editUserState.value = _editUserState.value.copy(lastName = event.lastName)
			}
			is EditUserEvent.PhoneChanged -> {
				_editUserState.value = _editUserState.value.copy(phone = event.phone)
			}
			EditUserEvent.Submit -> validateProfile()
			EditUserEvent.Reset -> {
				_editUserState.value = EditUserState()
			}
		}
	}

	fun onChangePasswordEvent(event: ChangePasswordEvent){
		when(event){
			is ChangePasswordEvent.CurrentPasswordChanged -> {
				_changePasswordState.value = _changePasswordState.value.copy(currentPassword = event.currentPassword)
			}
			is ChangePasswordEvent.CurrentPasswordVisible -> {
				_changePasswordState.value = _changePasswordState.value.copy(currentPasswordVisible = event.currentPasswordVisible)
			}
			is ChangePasswordEvent.NewPasswordChanged -> {
				_changePasswordState.value = _changePasswordState.value.copy(newPassword = event.newPassword)
			}
			is ChangePasswordEvent.NewPasswordVisible -> {
				_changePasswordState.value = _changePasswordState.value.copy(newPasswordVisible = event.newPasswordVisible)
			}
			ChangePasswordEvent.Submit -> validatePassword()
			ChangePasswordEvent.Reset -> {
				_changePasswordState.value = ChangePasswordState()
			}
		}
	}

	fun onChangeTheme(themeConfig: ThemeConfig){
		_uiSetting.value = _uiSetting.value.copy(theme = themeConfig)
		viewModelScope.launch {
			updateThemeUseCase(themeConfig)
		}
	}

	private fun deleteAccount(){
		deleteAccountUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_profileState.value = _profileState.value.copy(
						isLoading = false,
						isError = true,
						message = it.exception.message.toString()
					)
				}
				is NetworkResponseState.Loading -> {
					_profileState.value = _profileState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					_profileState.value = _profileState.value.copy(
						isLoading = false,
						isSuccess = true,
						message = it.result
					)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun logout(){
		logoutUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_profileState.value = _profileState.value.copy(
						isLoading = false,
						isError = true,
						message = it.exception.message.toString()
					)
				}
				is NetworkResponseState.Loading -> {
					_profileState.value = _profileState.value.copy(
						isLoading = true
					)
				}
				is NetworkResponseState.Success -> {
					_profileState.value = _profileState.value.copy(
						isLoading = false,
						isSuccess = true,
						message = it.result
					)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun checkLoggedIn(){
		getLoggedInStateUseCase().onEach {
			if (it){
				getApiUser()
			}
			_profileState.value = _profileState.value.copy(isLoggedIn = it)
		}.launchIn(viewModelScope)
	}

	private fun getPrefUser(){
		getPrefUserUseCase().onEach {
			_uiUser.value =  userPrefToUiMapper.map(it)
		}.launchIn(viewModelScope)
	}

	private fun getApiUser(){
		getApiUserUseCase().onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_profileState.value = _profileState.value.copy(
						isLoading = false,
						isError = true,
						message = it.exception.message.toString()
					)
				}
				NetworkResponseState.Loading -> {
					_profileState.value = _profileState.value.copy(isLoading = true)
				}
				is NetworkResponseState.Success -> {
					_profileState.value = _profileState.value.copy(isLoading = false)
					_uiUser.value = userApiToUiMapper.map(it.result)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun getSetting(){
		preferenceRepository.getSetting().onEach {
			_uiSetting.value = _uiSetting.value.copy(theme = it.theme)
		}.launchIn(viewModelScope)
	}

	private fun validateProfile(){
		val firstNameResult = validateInputUseCase.validateName(_editUserState.value.firstName)
		val lastNameResult = validateInputUseCase.validateName(_editUserState.value.lastName)
		val emailResult = validateInputUseCase.validateEmail(_editUserState.value.email)
		val phoneResult = validateInputUseCase.validatePhone(_editUserState.value.phone)

		_editUserState.value = _editUserState.value.copy(
			firstNameError = firstNameResult.errorMessage,
			lastNameError = lastNameResult.errorMessage,
			emailError = emailResult.errorMessage,
			phoneError = phoneResult.errorMessage
		)

		if (firstNameResult.successful &&
			lastNameResult.successful &&
			emailResult.successful &&
			phoneResult.successful
		){
			updateUserUseCase(
				UpdateProfileData(
					firstName = _editUserState.value.firstName,
					lastName = _editUserState.value.lastName,
					email = _editUserState.value.email,
					phone = _editUserState.value.phone,
				)
			).onEach {
				when(it){
					is NetworkResponseState.Error -> {
						_editUserState.value = _editUserState.value.copy(
							isLoading = false,
							isError = true,
							message = it.exception.message.toString()
						)
						_profileState.value = _profileState.value.copy(
							editUser = false
						)
					}
					is NetworkResponseState.Loading -> {
						_editUserState.value = _editUserState.value.copy(isLoading = true)
					}
					is NetworkResponseState.Success -> {
						_editUserState.value = _editUserState.value.copy(
							isLoading = false,
							isSuccess = true,
							message = it.result
						)
						_profileState.value = _profileState.value.copy(
							editUser = false
						)
					}
				}
			}.launchIn(viewModelScope)
		}
	}

	private fun uploadProfileImage(){
		_editUserState.value.imageUri?.let {
			updateUserUseCase.invoke(it).onEach { result ->
				when(result){
					is NetworkResponseState.Error -> {
						_editUserState.value = _editUserState.value.copy(
							isUploading = false,
							isError = true,
							message = "Could not upload the image"
						)
						_profileState.value = _profileState.value.copy(
							editUser = false
						)
					}
					is  NetworkResponseState.Loading -> {
						_editUserState.value = _editUserState.value.copy(isUploading = true)
					}
					is NetworkResponseState.Success -> {
						_editUserState.value = _editUserState.value.copy(
							isUploading = false,
							isSuccess = true,
							message = "Profile image updated successfully"
						)
						_profileState.value = _profileState.value.copy(
							editUser = false
						)
					}
				}
			} .launchIn(viewModelScope)
		}
	}

	private fun validatePassword(){
		val currentPasswordResult = validateInputUseCase.validatePassword(_changePasswordState.value.currentPassword)
		val newPasswordResult = validateInputUseCase.validatePassword(_changePasswordState.value.newPassword)

		_changePasswordState.value = _changePasswordState.value.copy(
			currentPasswordError = currentPasswordResult.errorMessage,
			newPasswordError = newPasswordResult.errorMessage
		)
		if (currentPasswordResult.successful && newPasswordResult.successful){
			changePasswordUseCase(
				currentPassword = _changePasswordState.value.currentPassword,
				newPassword = _changePasswordState.value.newPassword
			).onEach {
				when(it){
					is NetworkResponseState.Loading -> {
						_changePasswordState.value = _changePasswordState.value.copy(isLoading = true)
					}
					is NetworkResponseState.Error -> {
						_changePasswordState.value = _changePasswordState.value.copy(
							isLoading = false,
							isError = true,
							message = it.exception.message.toString()
						)
						_profileState.value = _profileState.value.copy(
							changePassword = false
						)
					}
					is NetworkResponseState.Success -> {
						_changePasswordState.value = _changePasswordState.value.copy(
							isLoading = false,
							isSuccess = true,
							message = it.result
						)
					}
				}
			}.launchIn(viewModelScope)
		}
	}
}