package com.example.deniseshop.core.data.datastore

import com.example.deniseshop.core.domain.model.ThemeMode
import com.example.deniseshop.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface SettingDataSource {
	suspend fun saveAuthToken(accessToken:String, refreshToken: String)
	suspend fun saveUser(user: User)
	suspend fun deleteUser()
	suspend fun saveUserImage(image: String)
	suspend fun saveSearchQuery(query: String)
	suspend fun deleteSearchQuery(query: String)
	suspend fun saveThemeMode(themeMode: ThemeMode)
	suspend fun saveWishlistItemId(id: Long)
	suspend fun deleteWishlistItem(id: Long)
	suspend fun clearWishlist()
	suspend fun saveCartItemId(id: Long)
	suspend fun deleteCartItem(id:Long)
	suspend fun clearCart()
	fun getUser(): Flow<User?>
	fun getAuthToken(): Flow<Map<String, String>>
	fun getThemeMode(): Flow<ThemeMode>
	fun getSearchQuery(): Flow<List<String>>
	fun getWishlistItems(): Flow<List<Long>>
	fun getCartItems(): Flow<List<Long>>
}