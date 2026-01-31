package com.example.deniseshop.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.deniseshop.core.presentation.designsystem.DeniseShopNavigationBar
import com.example.deniseshop.navigation.DeniseNavDisplay
import com.example.deniseshop.navigation.Navigator
import com.example.deniseshop.navigation.Route

@Composable
fun DeniseShopApp(
	appState: DeniseShopState
){
	val snackBarHostState = remember { SnackbarHostState() }

	val wishlistItems by appState.wishlistItems.collectAsStateWithLifecycle()
	val isLoggedIn by  appState.isLoggedIn.collectAsState(false)

	val navigator = remember {
		Navigator(
			backStack = appState.backStack,
			onNavigateToRestrictedKey = { Route.SignIn },
			isLoggedIn = {  isLoggedIn }
		)
	}

	Scaffold(
		bottomBar = {
			if (appState.currentTopLevelRoute != null){
				DeniseShopNavigationBar(
					topLevelRoutes = appState.topLeveRoutes,
					currentRoute = appState.currentTopLevelRoute,
					wishlistBadgeCount = wishlistItems.size,
					onRouteClick = {
						navigator.navigateTopLevel(it.route)
					}
				)
			}
		},
		contentWindowInsets = WindowInsets(0,0,0,0),
		snackbarHost = {
			SnackbarHost(
				hostState = snackBarHostState,
				modifier = Modifier
					.windowInsetsPadding(WindowInsets.navigationBars)
			)
		},
		containerColor = MaterialTheme.colorScheme.surface
	) { paddingValues ->
		Box(
			modifier = Modifier
				.padding(paddingValues)
				.windowInsetsPadding(
					WindowInsets.safeDrawing.only(
						WindowInsetsSides.Horizontal,
					),
				),
		) {
			DeniseNavDisplay(
				appState = appState,
				navigator = navigator,
				onShowSnackBar = { message, action ->
					snackBarHostState.showSnackbar(
						message = message,
						actionLabel = action,
						duration = SnackbarDuration.Short,
					) == SnackbarResult.ActionPerformed
				}
			)
		}
	}
}

