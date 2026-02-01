package com.example.deniseshop.feature.editprofile.presentation

import android.net.Uri
import com.example.deniseshop.core.presentation.UiText

data class EditProfileState(
	val firstName: String = "",
	val firstNameError: UiText? = null,
	val lastName: String = "",
	val lastNameError: UiText? = null,
	val email: String = "",
	val emailError: UiText? = null,
	val image: String = "",
	val imageUri: Uri? = null,
	val isUploading: Boolean = false,
	val phone: String = "",
	val phoneError: UiText? = null,
	val isLoading: Boolean = false,
	val error: UiText? = null,
	val success: Boolean = false,
)
