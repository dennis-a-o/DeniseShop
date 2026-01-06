package com.example.deniseshop.feature.editprofile.presentation

import android.content.Context
import android.net.Uri

sealed class EditProfileEvent {
	data class FirstNameChange(val fistName: String): EditProfileEvent()
	data class LastNameChange(val lastName: String): EditProfileEvent()
	data class EmailChange(val email: String): EditProfileEvent()
	data class PhoneChange(val phone: String): EditProfileEvent()
	data class ImageChange(val uri: Uri, val context: Context): EditProfileEvent()
	data object ResetErrorSuccessState: EditProfileEvent()
	data object Update : EditProfileEvent()
}