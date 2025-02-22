package com.example.deniseshop.di.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.deniseshop.data.api.ApiConstants
import com.example.deniseshop.data.api.BooleanAdapter
import com.example.deniseshop.data.api.DeniseShopApi
import com.example.deniseshop.data.api.interceptors.AuthenticationInterceptor
import com.example.deniseshop.data.source.PreferencesDataSource
import com.example.deniseshop.data.source.PreferencesDataSourceImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
	@Singleton
	@Provides
	fun provideApi(builder: Retrofit.Builder): DeniseShopApi{
		return  builder
			.build()
			.create(DeniseShopApi::class.java)
	}

	@Provides
	@Singleton
	fun provideMoshi(): Moshi {
		return Moshi.Builder()
			.add(BooleanAdapter())
			.addLast(KotlinJsonAdapterFactory())
			.build()
	}

	@Provides
	@Singleton
	fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit.Builder{
		return Retrofit.Builder()
			.baseUrl(ApiConstants.BASE_URL)
			.client(okHttpClient)
			.addConverterFactory(ScalarsConverterFactory.create())
			.addConverterFactory(MoshiConverterFactory.create(moshi))
	}

	@Provides
	@Singleton
	fun providesDataStorePreferencesRepository(dataStore: DataStore<Preferences>): PreferencesDataSource {
		return PreferencesDataSourceImpl(dataStore)
	}

	@Provides
	@Singleton
	fun providesAuthenticationInterceptor(dataStorePreferencesRepository: PreferencesDataSource): AuthenticationInterceptor{
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
}