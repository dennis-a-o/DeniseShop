package com.example.deniseshop.core.data.network

import com.example.deniseshop.core.data.dto.HomeDto
import com.example.deniseshop.core.data.dto.ImageDto
import com.example.deniseshop.core.data.dto.UserCredentialDto
import com.example.deniseshop.core.data.dto.UserDto
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.User
import com.example.deniseshop.core.domain.model.UserSignUp
import okhttp3.MultipartBody

interface RemoteDeniseShopDataSource {
	suspend fun signUp(user: UserSignUp): Result<Unit, DataError>
	suspend fun signIn(email: String, password: String): Result<UserCredentialDto, DataError>
	suspend fun forgotPassword(email: String): Result<Unit, DataError.Remote>
	suspend fun getUser(): Result<UserDto, DataError.Remote>
	suspend fun updateUser(user: User): Result<UserDto, DataError>
	suspend fun uploadUserImage(body: MultipartBody.Part): Result<ImageDto, DataError.Remote>
	suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit, DataError>
	suspend fun logout(): Result<Unit, DataError.Remote>
	suspend fun deleteUser(): Result<Unit, DataError.Remote>
	suspend fun getHome(): Result<HomeDto, DataError.Remote>
}