package com.example.deniseshop.data.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

object PreferencesKeys {
	val ACCESS_TOKEN = stringPreferencesKey("accessToken")
	val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
	val USER_ID = longPreferencesKey("userId")
	val USER_EMAIL = stringPreferencesKey("userEmail")
	val USER_PHONE = stringPreferencesKey("userPhone")
	val USER_FIRSTNAME = stringPreferencesKey("userFirstname")
	val USER_LASTNAME = stringPreferencesKey("userLastname")
	val USER_IMAGE = stringPreferencesKey("userImage")
	val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")
	val THEME = intPreferencesKey("themeConfig")
	val SEARCH_QUERY_SET =  stringSetPreferencesKey("searchQuerySet")
}