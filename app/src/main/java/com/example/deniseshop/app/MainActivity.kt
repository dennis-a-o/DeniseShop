package com.example.deniseshop.app

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
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
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.deniseshop.app.ui.DeniseShopApp
import com.example.deniseshop.app.ui.rememberDeniseShopAppState
import com.example.deniseshop.core.domain.model.ThemeMode
import com.example.deniseshop.core.domain.repository.UserSettingRepository
import com.example.deniseshop.core.presentation.theme.DeniseShopTheme
import com.example.deniseshop.navigation.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	@Inject
	lateinit var userSettingRepository: UserSettingRepository

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

		viewModel.sync()

		splashScreen.setKeepOnScreenCondition {
			when (uiState) {
				MainActivityUiState.Loading -> true
				is MainActivityUiState.Success -> false
			}
		}

		setContent {
			val darkTheme = shouldUseDarkTheme(uiState)

			val backStack =  rememberNavBackStack(Route.Home)

			DisposableEffect(backStack) {
				val listener = Consumer<Intent> { intent ->
					handlePaymentIntent(intent, backStack)
				}
				addOnNewIntentListener(listener)
				onDispose { removeOnNewIntentListener(listener) }
			}

			val appState = rememberDeniseShopAppState(
				backStack = backStack,
				settingRepository = userSettingRepository
			)

			DeniseShopTheme(darkTheme = darkTheme) {
				DeniseShopApp(
					appState = appState
				)
			}
		}
	}

	private fun handlePaymentIntent(
		intent: Intent,
		backStack: NavBackStack<NavKey>
	){
		val uri: Uri? = intent.data
		//I used package name as return host
		if (uri?.host == this.packageName) {
			val parameters = buildMap {
				uri.queryParameterNames?.forEach { argName ->
					this[argName] = uri.getQueryParameter(argName)
				}
			}

			if(
				parameters.containsKey("status") &&
				parameters.containsKey("token") &&
				parameters.containsKey("payerId")
			){
				backStack.add(
					Route.Checkout(
						status = parameters["status"] ?: "",
						token = parameters["token"] ?: "",
						payerId = parameters["payerId"] ?: "",
					)
				)
			}
		}
	}
}


@Composable
private fun shouldUseDarkTheme(
	uiState: MainActivityUiState,
): Boolean = when (uiState) {
	MainActivityUiState.Loading -> isSystemInDarkTheme()
	is MainActivityUiState.Success -> when(uiState.themeMode){
		ThemeMode.SYSTEM -> isSystemInDarkTheme()
		ThemeMode.DARK -> true
		ThemeMode.LIGHT -> false
	}
}


