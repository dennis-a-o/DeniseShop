package com.example.deniseshop.core.data.repository

import com.example.deniseshop.core.data.datastore.SettingDataSource
import com.example.deniseshop.core.domain.model.ThemeMode
import com.example.deniseshop.core.domain.repository.UserSettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineUserSettingRepository @Inject constructor(
	private val settingDataSource: SettingDataSource
): UserSettingRepository {
	override fun getThemeMode(): Flow<ThemeMode> {
		return settingDataSource.getThemeMode()
	}

	override suspend fun setThemeMode(themeMode: ThemeMode) {
		return settingDataSource.saveThemeMode(themeMode)
	}

	override fun getWishlistItems(): Flow<List<Long>> {
		return settingDataSource.getWishlistItems()
	}

	override suspend fun setWishlistItem(id: Long) {
		return settingDataSource.saveWishlistItemId(id)
	}

	override fun getCartItems(): Flow<List<Long>> {
		return settingDataSource.getCartItems()
	}

	override suspend fun setCartItem(id: Long) {
		return settingDataSource.saveCartItemId(id)
	}

	override fun getResentSearchQueries(): Flow<List<String>> {
		return settingDataSource.getSearchQuery()
	}

	override suspend fun setSearchQuery(query: String) {
		return settingDataSource.saveSearchQuery(query)
	}

	override suspend fun removeSearchQuery(query: String) {
		settingDataSource.deleteSearchQuery(query)
	}

}