package com.example.deniseshop.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.util.Consumer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.deniseshop.ui.models.ThemeConfig
import com.example.deniseshop.ui.theme.DeniseShopTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	private val viewModel: MainActivityViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		val splashScreen = installSplashScreen()
		enableEdgeToEdge(
			navigationBarStyle = SystemBarStyle.light(
				Color.TRANSPARENT, Color.TRANSPARENT
			)
		)
		super.onCreate(savedInstanceState)

		var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

		lifecycleScope.launch {
			viewModel.uiState.onEach { uiState = it }.collect()
		}

		splashScreen.setKeepOnScreenCondition {
			when (uiState) {
				MainActivityUiState.Loading -> true
				is MainActivityUiState.Success -> false
			}
		}

		setContent {
			val darkTheme = shouldUseDarkTheme(uiState)

			val navController = rememberNavController()

			DisposableEffect(navController) {
				val listener = Consumer<Intent> { intent ->
					navController.handleDeepLink(intent)
				}
				addOnNewIntentListener(listener)
				onDispose { removeOnNewIntentListener(listener) }
			}

			DeniseShopTheme(darkTheme = darkTheme) {
				DeniseShopApp(
					navController = navController,
				)
			}
		}
		createNotification(this)
	}

	private fun createNotification(context:Context){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
			val channel = NotificationChannel("download_channel","Downloads",NotificationManager.IMPORTANCE_DEFAULT).apply {
				description ="File Download Notification"
			}
			val notificationManager = context.getSystemService(NotificationManager::class.java)
			notificationManager.createNotificationChannel(channel)
		}
	}
}


@Composable
private fun shouldUseDarkTheme(
	uiState: MainActivityUiState,
): Boolean = when (uiState) {
	MainActivityUiState.Loading -> isSystemInDarkTheme()
	is MainActivityUiState.Success -> when(uiState.uiSetting.theme){
		ThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
		ThemeConfig.DARK -> true
		ThemeConfig.LIGHT -> false
		null -> isSystemInDarkTheme()
	}
}


