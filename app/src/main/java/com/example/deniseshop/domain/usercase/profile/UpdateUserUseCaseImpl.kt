package com.example.deniseshop.domain.usercase.profile

import android.content.Context
import android.net.Uri
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.domain.models.UpdateProfileData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class UpdateUserUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository,
	@ApplicationContext val context: Context
): UpdateUserUseCase {

	override  fun invoke(updateProfileData: UpdateProfileData): Flow<NetworkResponseState<String>> {
		return  apiRepository.updateUser(updateProfileData)
	}

	override  fun invoke(imageUri: Uri): Flow<NetworkResponseState<String>>{
		return try {
			val stream = context.contentResolver.openInputStream(imageUri)

			val imgPart = 	stream?.readBytes()?.toRequestBody("image/*".toMediaType())?.let {
				MultipartBody.Part.createFormData("image", "image.jpg", it)
			}

			if (imgPart != null){
				apiRepository.uploadProfileImage(imgPart)
			}else{
				flow { emit(NetworkResponseState.Error(Exception("Invalid image"))) }
			}
		}catch (e: Exception){
			flow { emit(NetworkResponseState.Error(e)) }
		}
	}
}