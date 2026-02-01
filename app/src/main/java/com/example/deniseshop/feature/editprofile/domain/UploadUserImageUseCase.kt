package com.example.deniseshop.feature.editprofile.domain

import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.repository.AuthRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadUserImageUseCase @Inject constructor(
	private val authRepository: AuthRepository
) {
	suspend operator fun invoke(
		body: MultipartBody.Part
	): Result<Unit, DataError.Remote>{
		return authRepository.uploadUserImage(body)
	}
}