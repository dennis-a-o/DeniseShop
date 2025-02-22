package com.example.deniseshop.di.source

import com.example.deniseshop.data.source.RemoteApiDataSource
import com.example.deniseshop.data.source.RemoteApiDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RemoteApiDataSourceModule {
	@Binds
	@ViewModelScoped
	abstract fun bindRemoteApiDataSource(
		dataSource: RemoteApiDataSourceImpl,
	): RemoteApiDataSource
}