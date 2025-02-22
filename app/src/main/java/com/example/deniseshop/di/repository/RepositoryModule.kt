package com.example.deniseshop.di.repository

import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.data.repository.ApiRepositoryImp
import com.example.deniseshop.data.repository.PreferenceRepository
import com.example.deniseshop.data.repository.PreferenceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
	@Binds
	@ViewModelScoped
	abstract fun bindApiRepository(
		apiRepositoryImp: ApiRepositoryImp
	): ApiRepository

	@Binds
	@ViewModelScoped
	abstract fun bindPreferenceRepository(
		preferenceRepositoryImpl: PreferenceRepositoryImpl
	): PreferenceRepository
}