package com.example.deniseshop.ui

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.deniseshop.core.presentation.designsystem.DeniseShopNavigationBar
import com.example.deniseshop.navigation.NavGraph
import com.example.deniseshop.navigation.TopLevelRoutes

@Composable
fun DeniseShopApp(
	navController: NavHostController,
){
	val snackBarHostState = remember { SnackbarHostState() }
	val currentDestination = navController.currentBackStackEntryAsState().value?.destination

	Scaffold(
		bottomBar = {
			if (TopLevelRoutes.entries.any { it.route == currentDestination?.route }){
				DeniseShopNavigationBar(
					onNavigate = { route, options ->
						navController.navigate(route, options)
					},
					currentRoute = currentDestination?.route ?: "",
					wishlistBadgeCount = 0
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
				navController = navController,
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

