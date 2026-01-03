package com.example.deniseshop.core.data.network

import com.example.deniseshop.core.data.dto.ImageDto
import com.example.deniseshop.core.data.dto.UserCredentialDto
import com.example.deniseshop.core.data.dto.UserDto
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.User
import com.example.deniseshop.core.domain.model.UserSignUp
import okhttp3.MultipartBody
import retrofit2.awaitResponse
import javax.inject.Inject

class RetrofitDeniseShopNetwork @Inject constructor(
	private val api: RetrofitDeniseShopNetworkApi
): RemoteDeniseShopDataSource {
	override suspend fun signUp(user: UserSignUp): Result<Unit, DataError> {
		return safeCallResponse <Unit> {
			api.signUp(
				firstName = user.firstName,
				lastName = user.lastName,
				email = user.email,
				phone = user.phone,
				password = user.password,
				acceptTerms = user.acceptTerms
			).awaitResponse()
		}
	}

	override suspend fun signIn(
		email: String,
		password: String
	): Result<UserCredentialDto, DataError> {
		return safeCallResponse <UserCredentialDto> {
			api.signIn(
				email = email,
				password = password
			).awaitResponse()
		}
	}

	override suspend fun forgotPassword(email: String): Result<Unit, DataError.Remote> {
		return safeCall<Unit> {
			api.forgotPassword(email)
		}
	}

	override suspend fun getUser(): Result<UserDto, DataError.Remote> {
		return safeCall<UserDto> {
			api.getUser()
		}
	}

	override suspend fun updateUser(user: User): Result<UserDto, DataError> {
		return safeCallResponse<UserDto> {
			api.updateUser(
				firstName = user.firstName,
				lastName = user.lastName,
				email = user.email,
				phone = user.phone,
			).awaitResponse()
		}
	}

	override suspend fun uploadUserImage(body: MultipartBody.Part): Result<ImageDto, DataError.Remote> {
		return safeCall<ImageDto> {
			api.uploadUserImage(body)
		}
	}

	override suspend fun changePassword(
		currentPassword: String,
		newPassword: String
	): Result<Unit, DataError> {
		return safeCallResponse<Unit> {
			api.changePassword(
				currentPassword = currentPassword,
				newPassword = newPassword
			).awaitResponse()
		}
	}

	override suspend fun logout(): Result<Unit, DataError.Remote> {
		return safeCall<Unit> {
			api.logout()
		}
	}

	override suspend fun deleteUser(): Result<Unit, DataError.Remote> {
		return safeCall<Unit> {
			api.deleteAccount()
		}
	}
}