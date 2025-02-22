package com.example.deniseshop.ui.models

import android.net.Uri

data class EditUserState(
	val firstName: String = "",
	val firstNameError: String? = null,
	val lastName: String = "",
	val lastNameError: String? = null,
	val email: String = "",
	val emailError: String? = null,
	val imageUri: Uri? = null,
	val isUploading: Boolean = false,
	val phone: String = "",
	val phoneError: String? = null,
	val isLoading: Boolean = false,
	val isError: Boolean = false,
	val isSuccess: Boolean = false,
	val message: String = ""
)
