package com.example.deniseshop.core.di

import com.example.deniseshop.core.data.datastore.SettingDataSource
import com.example.deniseshop.core.data.network.BASE_URL
import com.example.deniseshop.core.data.network.RetrofitDeniseShopNetworkApi
import com.example.deniseshop.core.data.network.interceptors.AuthenticationInterceptor
import com.example.deniseshop.data.source.PreferencesDataSource
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
	@Singleton
	@Provides
	fun providesJSON(): Json = Json {
		ignoreUnknownKeys = true
		isLenient = false
		explicitNulls = true
		coerceInputValues = true
	}

/*	@Provides
	@Singleton
	fun providesAuthenticationInterceptor(
		dataStorePreferencesRepository: PreferencesDataSource
	): AuthenticationInterceptor{
		return AuthenticationInterceptor(dataStorePreferencesRepository)
	}

	@Provides
	@Singleton
	fun provideOkHttpClient(
		authenticationInterceptor: AuthenticationInterceptor,
	): OkHttpClient{
		return OkHttpClient.Builder()
			.addInterceptor(authenticationInterceptor)
			.build()
	}

	@Provides
	@Singleton
	fun provideRetrofit(
		json: Json,
		okHttpClient: OkHttpClient
	): Retrofit.Builder {
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.client(okHttpClient)
			.addConverterFactory(
				json.asConverterFactory("application/json".toMediaType())
			)
	}*/

	@Singleton
	@Provides
	fun provideApi(
		//builder: Retrofit.Builder,
		json: Json,
		settingDataSource: SettingDataSource
	): RetrofitDeniseShopNetworkApi {
		val authenticationInterceptor = AuthenticationInterceptor(settingDataSource)

		val okHttpClient = OkHttpClient.Builder()
			.addInterceptor(authenticationInterceptor)
			.build()

		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.client(okHttpClient)
			.addConverterFactory(
				json.asConverterFactory("application/json".toMediaType())
			)
			.build()
			.create(RetrofitDeniseShopNetworkApi::class.java)
	}
}