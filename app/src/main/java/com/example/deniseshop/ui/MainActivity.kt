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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.deniseshop.ui.models.ThemeConfig
import com.example.deniseshop.ui.theme.DeniseShopTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	private val viewModel: MainActivityViewModel by viewModels()
	private var viewIntentData by mutableStateOf<Uri?>(null)

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

			DeniseShopTheme(darkTheme = darkTheme) {
				DeniseShopApp(
					viewIntentData = viewIntentData,
					onClearIntentData = { viewIntentData = null }
				)
			}
		}
		createNotification(this)
	}

	override fun onNewIntent(intent: Intent) {
		super.onNewIntent(intent)
		handleIntent(intent)
	}

	private fun handleIntent(intent: Intent){
		viewIntentData = if(intent.action == Intent.ACTION_VIEW && intent.scheme.equals("${application.packageName}.payments")){
			intent.data
		}else{
			null
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


