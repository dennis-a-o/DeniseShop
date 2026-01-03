package com.example.deniseshop.core.di

import com.example.deniseshop.core.data.datastore.SettingDataSource
import com.example.deniseshop.core.data.datastore.UserSettingDataSource
import com.example.deniseshop.core.data.network.RemoteDeniseShopDataSource
import com.example.deniseshop.core.data.network.RetrofitDeniseShopNetwork
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
}