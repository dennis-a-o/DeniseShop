package com.example.deniseshop.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.example.deniseshop.core.data.datastore.SettingDataSource
import com.example.deniseshop.core.data.network.BASE_URL
import com.example.deniseshop.core.data.network.RetrofitDeniseShopNetworkApi
import com.example.deniseshop.core.data.network.interceptors.AuthenticationInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.File
import javax.inject.Singleton

private const val USER_PREFERENCES = "deniseshop_user_preferences.preferences_pb"

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
	@Singleton
	@Provides
	fun providesJSON(): Json = Json {
		ignoreUnknownKeys = true
		isLenient = false
		explicitNulls = false
		coerceInputValues = true
	}

	@Provides
	@Singleton
	fun providesAuthenticationInterceptor(
		settingDataSource: SettingDataSource
	): AuthenticationInterceptor{
		return AuthenticationInterceptor(settingDataSource)
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
	}

	@Singleton
	@Provides
	fun provideApi(
		builder: Retrofit.Builder,
	): RetrofitDeniseShopNetworkApi {
		return builder
			.build()
			.create(RetrofitDeniseShopNetworkApi::class.java)
	}

	@Provides
	@Singleton
	internal  fun providesDeniseShopPreferenceDataStore(
		@ApplicationContext context: Context,
	): DataStore<Preferences> {

		return PreferenceDataStoreFactory.create(
			corruptionHandler = ReplaceFileCorruptionHandler(
				produceNewData = { emptyPreferences() }
			),
			scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
			produceFile = {
				File(context.filesDir, USER_PREFERENCES)
			},
		)
	}
}