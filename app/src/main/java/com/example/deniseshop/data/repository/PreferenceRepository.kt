package com.example.deniseshop.data.repository

import com.example.deniseshop.data.models.ApiToken
import com.example.deniseshop.data.models.ApiUser
import com.example.deniseshop.domain.models.PrefUser
import com.example.deniseshop.ui.models.ThemeConfig
import com.example.deniseshop.ui.models.UiSetting
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
	suspend fun setApiToken(apiToken: ApiToken)
	suspend fun setApiUser(apiUser: ApiUser)
	fun getPrefUser(): Flow<PrefUser>
	fun getApiToken(): Flow<ApiToken>
	suspend fun setAccessToken(token: String)
	fun getRefreshToken(): Flow<String>
	suspend fun clearApiUser()
	fun isLoggedIn(): Flow<Boolean>
	fun getSetting(): Flow<UiSetting>
	suspend fun setTheme(themeConfig: ThemeConfig)
	suspend fun setIsLoggedIn(isLoggedIn: Boolean)
	suspend fun setProfileImage(image: String)
	suspend fun saveSearchQuery(query: String)
	suspend fun deleteSearchQuery(query: String)
	fun getSearchQueryHistory(query: String): Flow<Set<String>>
}