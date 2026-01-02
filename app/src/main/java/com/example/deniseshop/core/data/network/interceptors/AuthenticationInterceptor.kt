package com.example.deniseshop.core.data.network.interceptors

import android.util.Log
import com.example.deniseshop.core.data.dto.AuthTokenDto
import com.example.deniseshop.core.data.network.AUTH_ENDPOINT
import com.example.deniseshop.core.data.network.AUTH_HEADER
import com.example.deniseshop.core.data.network.BASE_URL
import com.example.deniseshop.core.data.network.REFRESH_TOKEN
import com.example.deniseshop.core.data.network.TOKEN_TYPE
import com.example.deniseshop.data.api.ApiParameters
import com.example.deniseshop.data.source.PreferencesDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
	private val dataStorePreferencesRepository: PreferencesDataSource
): Interceptor {

	companion object {
		const val UNAUTHORIZED = 401
		const val OK = 200
	}

	override fun intercept(chain: Interceptor.Chain): Response {
		var accessToken: String
		var refreshToken: String
		val originalRequest = chain.request()

		runBlocking {
			val apiToken =  dataStorePreferencesRepository.getApiToken().first()
			accessToken = apiToken.accessToken.toString()
			refreshToken = apiToken.refreshToken.toString()
		}

		val authenticatedRequest = chain.request().newBuilder()
			.header(ApiParameters.AUTH_HEADER, ApiParameters.TOKEN_TYPE + accessToken)
			.build()

		val authenticatedResponse = chain.proceed(authenticatedRequest)

		if (authenticatedResponse.code == UNAUTHORIZED){
			authenticatedResponse.close()

			val tokenRefreshResponse = chain.refreshToken(refreshToken)

			if (tokenRefreshResponse.code == OK){
				val newToken = extractToken(tokenRefreshResponse)

				storeNewToken(newToken)

				tokenRefreshResponse.close()

				//Proceed with original request with updated token
				val newRequest = originalRequest.newBuilder()
					.header(ApiParameters.AUTH_HEADER, ApiParameters.TOKEN_TYPE + newToken.accessToken)
					.build()

				return chain.proceed(newRequest)
			}else if(tokenRefreshResponse.code == UNAUTHORIZED){
				//clear user
				deleteUserLocalCache()

				tokenRefreshResponse.close()
				//proceed with original request with authentication failure
				return chain.proceed(originalRequest)
			}else{
				tokenRefreshResponse.close()

				return chain.proceed(originalRequest)
			}
		}

		return authenticatedResponse
	}

	private fun Interceptor.Chain.refreshToken(refreshToken: String): Response{
		val url = request()
			.url
			.newBuilder(BASE_URL + AUTH_ENDPOINT + REFRESH_TOKEN)!!
			.build()

		val body = FormBody.Builder().build()

		val tokenRefreshRequest = request()
			.newBuilder()
			.post(body)
			.url(url)
			.header(AUTH_HEADER, TOKEN_TYPE + refreshToken)
			.build()

		return proceed(tokenRefreshRequest)
	}

	private fun extractToken(tokenRefreshResponse: Response): AuthTokenDto {
		return try {
			val responseBody = tokenRefreshResponse.body!!

			Json.decodeFromString<AuthTokenDto>(responseBody.string())
		}catch (e: Exception){
			Log.e("AuthenticationInterceptor","extractToken(): $e", e)
			AuthTokenDto(null,null)
		}
	}

	private fun storeNewToken(token: AuthTokenDto) {
		runBlocking {
			token.accessToken?.let {
				dataStorePreferencesRepository.setAccessToken(it)
			}
		}
	}

	private fun deleteUserLocalCache(){
		runBlocking {
			dataStorePreferencesRepository.clearApiUser()
		}
	}
}