package com.example.deniseshop.core.domain.repository

import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.User
import com.example.deniseshop.core.domain.model.UserSignUp
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface AuthRepository {
	suspend fun signUp(user: UserSignUp): Result<Unit, DataError>
	suspend fun signIn(email: String, password: String): Result<Unit, DataError>
	suspend fun forgotPassword(email: String): Result<Unit, DataError.Remote>
	fun getUser(): Flow<User?>
	suspend fun updateUser(user: User): Result<Unit, DataError>
	suspend fun uploadUserImage(body: MultipartBody.Part): Result<Unit, DataError.Remote>
	suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit, DataError>
	suspend fun logout(): Result<Unit, DataError.Remote>
	suspend fun deleteUser(): Result<Unit, DataError.Remote>
}