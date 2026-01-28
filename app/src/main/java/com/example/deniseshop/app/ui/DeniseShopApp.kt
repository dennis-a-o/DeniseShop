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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.deniseshop.core.presentation.designsystem.DeniseShopNavigationBar
import com.example.deniseshop.navigation.NavGraph

@Composable
fun DeniseShopApp(
	appState: DeniseShopState
){
	val snackBarHostState = remember { SnackbarHostState() }
	val wishlistItems by appState.wishlistItems.collectAsStateWithLifecycle()

	Scaffold(
		bottomBar = {
			if (appState.currentTopLevelRoute != null){
				DeniseShopNavigationBar(
					topLevelRoutes = appState.topLeveRoutes,
					currentRoute = appState.currentTopLevelRoute,
					wishlistBadgeCount = wishlistItems.size,
					onRouteClick = {
						appState.navigateToTopLevelRoute(it)
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
			NavGraph(
				appState = appState,
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

