package com.example.deniseshop.data.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.deniseshop.data.datastore.PreferencesKeys
import com.example.deniseshop.data.models.ApiToken
import com.example.deniseshop.data.models.ApiUser
import com.example.deniseshop.domain.models.PrefUser
import com.example.deniseshop.ui.models.ThemeConfig
import com.example.deniseshop.ui.models.UiSetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataSourceImpl @Inject constructor(
	private val dataStore: DataStore<Preferences>
): PreferencesDataSource {

	override suspend fun setApiToken(apiToken: ApiToken){
		dataStore. edit { prefs ->
			prefs[PreferencesKeys.REFRESH_TOKEN] = apiToken.refreshToken?:""
			prefs[PreferencesKeys.ACCESS_TOKEN] =  apiToken.accessToken ?:""
		}
	}

	override suspend fun setApiUser(apiUser: ApiUser) {
		dataStore.edit { prefs ->
			prefs[PreferencesKeys.USER_ID] = apiUser.id?: -1
			prefs[PreferencesKeys.USER_EMAIL] = apiUser.email?:""
			prefs[PreferencesKeys.USER_PHONE] = apiUser.phone?:""
			prefs[PreferencesKeys.USER_FIRSTNAME] = apiUser.firstName?:""
			prefs[PreferencesKeys.USER_LASTNAME] = apiUser.lastName?:""
			prefs[PreferencesKeys.USER_IMAGE] = apiUser.image?:""
		}
	}

	override  fun getPrefUser(): Flow<PrefUser>  = dataStore.data.map { prefs ->
		PrefUser(
			id = prefs[PreferencesKeys.USER_ID],
			email = prefs[PreferencesKeys.USER_EMAIL],
			phone =prefs[PreferencesKeys.USER_PHONE],
			firstName = prefs[PreferencesKeys.USER_FIRSTNAME],
			lastName = prefs[PreferencesKeys.USER_LASTNAME],
			image = prefs[PreferencesKeys.USER_IMAGE],
		)
	}

	override fun getApiToken(): Flow<ApiToken> = dataStore.data.map { prefs ->
		ApiToken(
			refreshToken = prefs[PreferencesKeys.REFRESH_TOKEN],
			accessToken = prefs[PreferencesKeys.ACCESS_TOKEN],
		)
	}

	override suspend fun setAccessToken(token: String) {
		dataStore.edit { prefs ->
			prefs[PreferencesKeys.ACCESS_TOKEN] = token
		}
	}

	override fun getRefreshToken(): Flow<String> = dataStore.data.map { prefs ->
		prefs[PreferencesKeys.REFRESH_TOKEN] ?: ""
	}

	override suspend fun clearApiUser() {
		dataStore.edit { prefs ->
			prefs[PreferencesKeys.USER_ID] = -1L
			prefs[PreferencesKeys.USER_EMAIL] = ""
			prefs[PreferencesKeys.USER_PHONE] = ""
			prefs[PreferencesKeys.USER_FIRSTNAME] = ""
			prefs[PreferencesKeys.USER_LASTNAME] = ""
			prefs[PreferencesKeys.USER_IMAGE] = ""
			prefs[PreferencesKeys.REFRESH_TOKEN] = ""
			prefs[PreferencesKeys.ACCESS_TOKEN] = ""
			prefs[PreferencesKeys.IS_LOGGED_IN] = false
		}
	}

	override fun isLoggedIn(): Flow<Boolean> = dataStore.data.map{ prefs ->
		prefs[PreferencesKeys.IS_LOGGED_IN]?: false
	}

	override fun getSetting(): Flow<UiSetting> = dataStore.data.map { prefs ->
		UiSetting(
			theme = when(prefs[PreferencesKeys.THEME]){
				1 -> ThemeConfig.LIGHT
				2 -> ThemeConfig.DARK
				else -> ThemeConfig.FOLLOW_SYSTEM
			}
		)
	}

	override suspend fun setTheme(themeConfig: ThemeConfig) {
		dataStore.edit { prefs ->
			prefs[PreferencesKeys.THEME] = when(themeConfig){
				ThemeConfig.LIGHT -> 1
				ThemeConfig.DARK -> 2
				else -> 0
			}
		}
	}

	override suspend fun setIsLoggedIn(isLoggedIn: Boolean) {
		dataStore.edit { prefs ->
			prefs[PreferencesKeys.IS_LOGGED_IN] = isLoggedIn

		}
	}

	override suspend fun setProfileImage(image: String) {
		dataStore.edit { prefs ->
			prefs[PreferencesKeys.USER_IMAGE] = image
		}
	}

	override suspend fun saveSearchQuery(query: String) {
		dataStore.edit { prefs ->
			val updatedQuerySet = mutableSetOf<String>()
			val querySet: Set<String> = prefs[PreferencesKeys.SEARCH_QUERY_SET]?: emptySet()

			updatedQuerySet.addAll(querySet)
			//store up 40 only
			if (updatedQuerySet.size > 40){
				updatedQuerySet.remove(updatedQuerySet.first())
			}
			updatedQuerySet.add(query)

			prefs[PreferencesKeys.SEARCH_QUERY_SET] = updatedQuerySet
		}
	}

	override suspend fun deleteSearchQuery(query: String) {
		dataStore.edit { prefs ->
			val updatedQuerySet = mutableSetOf<String>()
			val querySet: Set<String> = prefs[PreferencesKeys.SEARCH_QUERY_SET]?: emptySet()

			updatedQuerySet.addAll(querySet)
			updatedQuerySet.remove(query)

			prefs[PreferencesKeys.SEARCH_QUERY_SET] = updatedQuerySet
		}
	}

	override fun getSearchQueryHistory(query: String): Flow<Set<String>> {
		return dataStore.data.map { prefs ->
			prefs[PreferencesKeys.SEARCH_QUERY_SET]?.filter { it.startsWith(query) }?.toSet() ?: emptySet()
		}
	}

}