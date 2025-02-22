package com.example.deniseshop.data.api.interceptors

import android.util.Log
import com.example.deniseshop.data.api.ApiConstants
import com.example.deniseshop.data.api.ApiParameters
import com.example.deniseshop.data.models.ApiToken
import com.example.deniseshop.data.source.PreferencesDataSource
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.FormBody
import okhttp3.HttpUrl
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
				val newToken = mapToken(tokenRefreshResponse)
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
			.newBuilder(ApiConstants.BASE_URL + ApiConstants.AUTH_ENDPOINT + ApiConstants.REFRESH_TOKEN)!!
			.build()

		val body = FormBody.Builder().build()

		val tokenRefreshRequest = request()
			.newBuilder()
			.post(body)
			.url(url)
			.header(ApiParameters.AUTH_HEADER, ApiParameters.TOKEN_TYPE + refreshToken)
			.build()

		return proceed(tokenRefreshRequest)
	}

	private fun mapToken(tokenRefreshResponse: Response): ApiToken {
		val moshi = Moshi.Builder().build()
		val tokenAdapter = moshi.adapter(ApiToken::class.java)
		val responseBody = tokenRefreshResponse.body!! // if successful, this should be good :]

		return tokenAdapter.fromJson(responseBody.string()) ?: ApiToken(null, null)
	}

	private fun storeNewToken(apiToken: ApiToken) {
		runBlocking {
			dataStorePreferencesRepository.setAccessToken(apiToken.accessToken.toString())
		}
	}

	private fun deleteUserLocalCache(){
		runBlocking {
			dataStorePreferencesRepository.clearApiUser()
		}
	}
}
