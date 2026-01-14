package com.example.deniseshop.core.domain.repository

import com.example.deniseshop.core.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface UserSettingRepository {
	fun getThemeMode(): Flow<ThemeMode>
	suspend fun setThemeMode(themeMode: ThemeMode)
	fun getWishlistItems(): Flow<List<Long>>
	suspend fun setWishlistItem(id: Long)
	fun getCartItems(): Flow<List<Long>>
	suspend fun setCartItem(id: Long)
	fun getResentSearchQueries(): Flow<List<String>>
	suspend fun setSearchQuery(query: String)
	suspend fun removeSearchQuery(query: String)
}