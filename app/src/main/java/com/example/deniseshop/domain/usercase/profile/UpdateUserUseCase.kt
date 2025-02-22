package com.example.deniseshop.domain.usercase.profile

import android.net.Uri
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.domain.models.UpdateProfileData
import kotlinx.coroutines.flow.Flow

interface UpdateUserUseCase {
	operator fun invoke(updateProfileData: UpdateProfileData): Flow<NetworkResponseState<String>>
	operator fun invoke(imageUri: Uri): Flow<NetworkResponseState<String>>
}