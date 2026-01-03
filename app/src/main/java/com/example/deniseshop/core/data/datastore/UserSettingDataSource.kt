package com.example.deniseshop.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.deniseshop.core.domain.model.ThemeMode
import com.example.deniseshop.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserSettingDataSource @Inject constructor(
	private val dataStore: DataStore<Preferences>
): SettingDataSource {

	companion object {
		val ACCESS_TOKEN = stringPreferencesKey("accessToken")
		val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
		val USER_ID = longPreferencesKey("userId")
		val USER_EMAIL = stringPreferencesKey("userEmail")
		val USER_PHONE = stringPreferencesKey("userPhone")
		val USER_FIRSTNAME = stringPreferencesKey("userFirstname")
		val USER_LASTNAME = stringPreferencesKey("userLastname")
		val USER_IMAGE = stringPreferencesKey("userImage")
		val THEME_MODE = intPreferencesKey("themeMode")
		val SEARCH_QUERY_SET = stringSetPreferencesKey("searchQuerySet")
		val WISHLIST_ID_SET = stringSetPreferencesKey("wishlistIdSet")
		val CART_ID_SET = stringSetPreferencesKey("cartIdSet")
	}

	override suspend fun saveAuthToken(accessToken: String, refreshToken: String) {
		dataStore.edit { pref ->
			pref[ACCESS_TOKEN] = accessToken
			pref[REFRESH_TOKEN] = refreshToken
		}
	}

	override suspend fun saveUser(user: User) {
		dataStore.edit { prefs ->
			prefs[USER_ID] = user.id
			prefs[USER_FIRSTNAME] = user.firstName
			prefs[USER_LASTNAME] = user.lastName
			prefs[USER_EMAIL] = user.email
			prefs[USER_PHONE] = user.phone
			prefs[USER_IMAGE] = user.image
		}
	}

	override suspend fun deleteUser() {
		dataStore.edit { prefs ->
			prefs[USER_ID] = 0
			prefs[USER_FIRSTNAME] = ""
			prefs[USER_LASTNAME] = ""
			prefs[USER_EMAIL] = ""
			prefs[USER_PHONE] = ""
			prefs[USER_IMAGE] = ""
		}
	}

	override suspend fun saveUserImage(image: String) {
		dataStore.edit { prefs ->
			prefs[USER_IMAGE] = image
		}
	}

	override suspend fun saveSearchQuery(query: String) {
		dataStore.edit { prefs ->
			val currentSearchQueries = prefs[SEARCH_QUERY_SET]?.toMutableSet() ?: mutableSetOf("")

			if (currentSearchQueries.size > 20){
				currentSearchQueries.remove(currentSearchQueries.first())
			}

			currentSearchQueries.add(query)

			prefs[SEARCH_QUERY_SET] = currentSearchQueries
		}
	}

	override suspend fun deleteSearchQuery(query: String) {
		dataStore.edit { prefs ->
			val currentSearchQueries = prefs[SEARCH_QUERY_SET]?.toMutableSet() ?: mutableSetOf("")

			currentSearchQueries.remove(query)

			prefs[SEARCH_QUERY_SET] = currentSearchQueries
		}
	}

	override suspend fun saveThemeMode(themeMode: ThemeMode) {
		dataStore.edit { prefs ->
			prefs[THEME_MODE] = when(themeMode) {
				ThemeMode.SYSTEM -> 0
				ThemeMode.DARK -> 1
				ThemeMode.LIGHT -> 2
			}
		}
	}

	override suspend fun saveWishlistItemId(id: Long) {
		dataStore.edit { prefs ->
			val currentSet = prefs[WISHLIST_ID_SET]?.toMutableSet() ?: mutableSetOf()

			currentSet.add("$id")

			prefs[WISHLIST_ID_SET] = currentSet
		}
	}

	override suspend fun deleteWishlistItem(id: Long) {
		dataStore.edit { prefs ->
			val currentSet = prefs[WISHLIST_ID_SET]?.toMutableSet() ?: mutableSetOf()

			currentSet.remove("$id")

			prefs[WISHLIST_ID_SET] = currentSet
		}
	}

	override suspend fun clearWishlist() {
		dataStore.edit { prefs ->
			prefs[WISHLIST_ID_SET] = emptySet<String>()
		}
	}

	override suspend fun saveCartItemId(id: Long) {
		dataStore.edit { prefs ->
			val currentSet = prefs[CART_ID_SET]?.toMutableSet() ?: mutableSetOf()

			currentSet.add("$id")

			prefs[CART_ID_SET] = currentSet
		}
	}

	override suspend fun deleteCartItem(id: Long) {
		dataStore.edit { prefs ->
			val currentSet = prefs[CART_ID_SET]?.toMutableSet() ?: mutableSetOf()

			currentSet.remove("$id")

			prefs[CART_ID_SET] = currentSet
		}
	}

	override suspend fun clearCart() {
		dataStore.edit { prefs ->
			prefs[CART_ID_SET] = emptySet<String>()
		}
	}

	override fun getUser(): Flow<User?> {
		return dataStore.data.map { prefs ->
			if (
				(prefs[USER_ID] == null || prefs[USER_ID] == 0L) &&
				(prefs[USER_EMAIL] == null || prefs[USER_EMAIL] == "")
			){
				null
			}else {
				User(
					id = prefs[USER_ID] ?: 0,
					firstName = prefs[USER_FIRSTNAME] ?: "",
					lastName = prefs[USER_LASTNAME] ?: "",
					email = prefs[USER_EMAIL] ?: "",
					phone = prefs[USER_PHONE] ?: "",
					image = prefs[USER_IMAGE] ?: "",
				)
			}
		}
	}

	override fun getAuthToken(): Flow<Map<String, String>> {
		return dataStore.data.map { prefs ->
			mapOf(
				"accessToken" to  (prefs[ACCESS_TOKEN] ?: ""),
				"refreshToken" to  (prefs[REFRESH_TOKEN] ?: ""),
			)
		}
	}

	override fun getThemeMode(): Flow<ThemeMode> {
		return dataStore.data.map { prefs ->
			when(prefs[THEME_MODE]){
				1 -> ThemeMode.DARK
				2 -> ThemeMode.LIGHT
				else -> ThemeMode.SYSTEM
			}
		}
	}

	override fun getSearchQuery(): Flow<List<String>> {
		return dataStore.data.map { prefs ->
			prefs[SEARCH_QUERY_SET]?.toList() ?: emptyList()
		}
	}

	override fun getWishlistItems(): Flow<List<Long>> {
		return dataStore.data.map { prefs ->
			prefs[WISHLIST_ID_SET]?.toList()?.map { it.toLong() } ?: emptyList()
		}
	}

	override fun getCartItems(): Flow<List<Long>> {
		return dataStore.data.map { prefs ->
			prefs[CART_ID_SET]?.toList()?.map { it.toLong() } ?: emptyList()
		}
	}
}