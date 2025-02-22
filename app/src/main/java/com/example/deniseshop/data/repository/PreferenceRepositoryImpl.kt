package com.example.deniseshop.data.repository

import com.example.deniseshop.data.models.ApiToken
import com.example.deniseshop.data.models.ApiUser
import com.example.deniseshop.data.source.PreferencesDataSource
import com.example.deniseshop.domain.models.PrefUser
import com.example.deniseshop.ui.models.ThemeConfig
import com.example.deniseshop.ui.models.UiSetting
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(
	private val preferencesDataSource: PreferencesDataSource
): PreferenceRepository {
	override suspend fun setApiToken(apiToken: ApiToken) = preferencesDataSource.setApiToken(apiToken)

	override suspend fun setApiUser(apiUser: ApiUser) {
		preferencesDataSource.setApiUser(apiUser)
	}

	override fun getPrefUser(): Flow<PrefUser> = preferencesDataSource.getPrefUser()

	override fun getApiToken(): Flow<ApiToken> = preferencesDataSource.getApiToken()

	override suspend fun setAccessToken(token: String) {
		preferencesDataSource.setAccessToken(token)
	}

	override fun getRefreshToken(): Flow<String> = preferencesDataSource.getRefreshToken()

	override suspend fun clearApiUser() {
		preferencesDataSource.clearApiUser()
	}

	override fun isLoggedIn(): Flow<Boolean> = preferencesDataSource.isLoggedIn()

	override fun getSetting(): Flow<UiSetting> = preferencesDataSource.getSetting()


	override suspend fun setTheme(themeConfig: ThemeConfig) {
		preferencesDataSource.setTheme(themeConfig)
	}

	override suspend fun setIsLoggedIn(isLoggedIn: Boolean) {
		preferencesDataSource.setIsLoggedIn(isLoggedIn)
	}

	override suspend fun setProfileImage(image: String) {
		preferencesDataSource.setProfileImage(image)
	}

	override suspend fun saveSearchQuery(query: String) {
		preferencesDataSource.saveSearchQuery(query)
	}

	override suspend fun deleteSearchQuery(query: String) {
		preferencesDataSource.deleteSearchQuery(query)
	}

	override fun getSearchQueryHistory(query: String): Flow<Set<String>> = preferencesDataSource.getSearchQueryHistory(query)
}