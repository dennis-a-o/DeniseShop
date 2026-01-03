package com.example.deniseshop.core.data.repository

import com.example.deniseshop.core.data.datastore.SettingDataSource
import com.example.deniseshop.core.data.mappers.toUser
import com.example.deniseshop.core.data.network.RemoteDeniseShopDataSource
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.User
import com.example.deniseshop.core.domain.model.UserSignUp
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
	private val remote: RemoteDeniseShopDataSource,
	private val preferenceDataSource: SettingDataSource
): AuthRepository {
	override suspend fun signUp(user: UserSignUp): Result<Unit, DataError> {
		return remote.signUp(user)
	}

	override suspend fun signIn(
		email: String,
		password: String
	): Result<Unit, DataError> {
		return when(val result = remote.signIn(email, password)) {
			is Result.Error -> Result.Error(result.error)
			is Result.Success -> {
				val data = result.data

				preferenceDataSource.saveAuthToken(
					accessToken = data.authToken.accessToken.toString(),
					refreshToken = data.authToken.refreshToken.toString()
				)

				preferenceDataSource.saveUser(data.user.toUser())

				Result.Success(Unit)
			}
		}
	}

	override suspend fun forgotPassword(email: String): Result<Unit, DataError.Remote> {
		return remote.forgotPassword(email)
	}

	override fun getUser(): Flow<User?> = flow {
		remote.getUser()
			.onSuccess {
				preferenceDataSource.saveUser(it.toUser())
			}

		preferenceDataSource.getUser().collect {
			emit(it)
		}
	}

	override suspend fun updateUser(user: User): Result<Unit, DataError> {
		return when(val result = remote.updateUser(user)) {
			is Result.Error -> Result.Error(result.error)
			is Result.Success -> {
				val userDto = result.data

				preferenceDataSource.saveUser(userDto.toUser())

				Result.Success(Unit)
			}
		}
	}

	override suspend fun uploadUserImage(body: MultipartBody.Part): Result<Unit, DataError.Remote> {
		return when(val result = remote.uploadUserImage(body)) {
			is Result.Error -> Result.Error(result.error)
			is Result.Success -> {
				val imageDto = result.data

				preferenceDataSource.saveUserImage(imageDto.url)

				Result.Success(Unit)
			}
		}
	}

	override suspend fun changePassword(
		currentPassword: String,
		newPassword: String
	): Result<Unit, DataError> {
		return remote.changePassword(
			currentPassword = currentPassword,
			newPassword = newPassword
		)
	}

	override suspend fun logout(): Result<Unit, DataError.Remote> {
		return remote.logout()
	}

	override suspend fun deleteUser(): Result<Unit, DataError.Remote> {
		return remote.deleteUser()
	}
}