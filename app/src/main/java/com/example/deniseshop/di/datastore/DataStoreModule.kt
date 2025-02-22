package com.example.deniseshop.di.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.File
import javax.inject.Singleton

private const val USER_PREFERENCES = "deniseshop_user_preferences.preferences_pb"



@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
	@Provides
	@Singleton
	internal  fun providesDeniseShopPreferenceDataStore(
		@ApplicationContext context: Context,
	): DataStore<Preferences> {

		return PreferenceDataStoreFactory.create(
			corruptionHandler = ReplaceFileCorruptionHandler(
				produceNewData = { emptyPreferences() }
			),
			scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
			produceFile = {
				File(context.filesDir, USER_PREFERENCES)
			},
		)
	}
}