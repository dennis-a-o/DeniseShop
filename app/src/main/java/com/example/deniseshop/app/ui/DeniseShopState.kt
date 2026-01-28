package com.example.deniseshop.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.deniseshop.core.domain.repository.UserSettingRepository
import com.example.deniseshop.navigation.TopLevelRoutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberDeniseShopAppState(
	settingRepository: UserSettingRepository,
	coroutineScope: CoroutineScope = rememberCoroutineScope(),
	navController: NavHostController = rememberNavController(),
): DeniseShopState {
	return remember(
		navController,
		coroutineScope,
		settingRepository
	) {
		DeniseShopState(
			navController = navController,
			coroutineScope = coroutineScope,
			userSettingRepository = settingRepository
		)
	}
}
@Stable
class DeniseShopState(
	val navController: NavHostController,
	coroutineScope: CoroutineScope,
	userSettingRepository: UserSettingRepository
) {
	val currentRoute: NavDestination?
		@Composable get() = navController
			.currentBackStackEntryAsState().value?.destination

	val currentTopLevelRoute: TopLevelRoutes?
		@Composable get() = when (currentRoute?.route) {
			TopLevelRoutes.HOME.route -> TopLevelRoutes.HOME
			TopLevelRoutes.CATEGORY.route -> TopLevelRoutes.CATEGORY
			TopLevelRoutes.WISHLIST.route -> TopLevelRoutes.WISHLIST
			TopLevelRoutes.PROFILE.route -> TopLevelRoutes.PROFILE
			else -> null
		}

	val topLeveRoutes: List<TopLevelRoutes> = TopLevelRoutes.entries

	val wishlistItems = userSettingRepository.getWishlistItems()
		.stateIn(
			coroutineScope,
			SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList(),
		)

	fun navigateToTopLevelRoute(topLevelRoute: TopLevelRoutes) {
		val topLevelNavOptions = navOptions {
			popUpTo(navController.graph.findStartDestination().id) {
				saveState = true
			}

			launchSingleTop = true

			restoreState = true
		}

		when (topLevelRoute) {
			TopLevelRoutes.HOME -> navController.navigate(TopLevelRoutes.HOME.route, topLevelNavOptions)
			TopLevelRoutes.CATEGORY -> navController.navigate(TopLevelRoutes.CATEGORY.route, topLevelNavOptions)
			TopLevelRoutes.WISHLIST -> navController.navigate(TopLevelRoutes.WISHLIST.route, topLevelNavOptions)
			TopLevelRoutes.PROFILE-> navController.navigate(TopLevelRoutes.PROFILE.route, topLevelNavOptions)
		}
	}
}