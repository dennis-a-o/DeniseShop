package com.example.deniseshop.core.di

import com.example.deniseshop.core.data.datastore.SettingDataSource
import com.example.deniseshop.core.data.datastore.UserSettingDataSource
import com.example.deniseshop.core.data.network.RemoteDeniseShopDataSource
import com.example.deniseshop.core.data.network.RetrofitDeniseShopNetwork
import com.example.deniseshop.core.data.repository.AuthRepositoryImpl
import com.example.deniseshop.core.data.repository.OfflineUserSettingRepository
import com.example.deniseshop.core.data.repository.RemoteShopRepository
import com.example.deniseshop.core.domain.repository.AuthRepository
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.core.domain.repository.UserSettingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreBindModule {
	@Singleton
	@Binds
	abstract fun bindsRetrofitDeniseShopNetwork(
		network: RetrofitDeniseShopNetwork
	): RemoteDeniseShopDataSource

	@Singleton
	@Binds
	abstract fun bindsUserSettingDataStore(
		settingDataSource: UserSettingDataSource
	): SettingDataSource

	@Singleton
	@Binds
	abstract fun bindsAuthRepositoryImpl(
		authRepositoryImpl: AuthRepositoryImpl
	): AuthRepository

	@Singleton
	@Binds
	abstract fun bindsOfflineUserSettingRepository(
		offlineUserSettingRepository: OfflineUserSettingRepository
	): UserSettingRepository

	@Singleton
	@Binds
	abstract fun bindsRemoteShopRepository(
		remoteShopRepository: RemoteShopRepository
	): ShopRepository
}