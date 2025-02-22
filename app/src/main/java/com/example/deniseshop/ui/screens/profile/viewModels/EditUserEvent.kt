package com.example.deniseshop.ui.screens.profile.viewModels

import android.net.Uri

sealed class EditUserEvent {
	data class FirstNameChanged(val fistName: String): EditUserEvent()
	data class LastNameChanged(val lastName: String): EditUserEvent()
	data class EmailChanged(val email: String): EditUserEvent()
	data class PhoneChanged(val phone: String): EditUserEvent()
	data class ImageChanged(val imageUri: Uri): EditUserEvent()
	data object Reset: EditUserEvent()
	data object Submit : EditUserEvent()
}

