package com.example.deniseshop.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.deniseshop.core.domain.repository.UserSettingRepository
import com.example.deniseshop.navigation.Route
import com.example.deniseshop.navigation.TopLevelRoutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberDeniseShopAppState(
	settingRepository: UserSettingRepository,
	coroutineScope: CoroutineScope = rememberCoroutineScope(),
	backStack: NavBackStack<NavKey> =  rememberNavBackStack(Route.Home),
): DeniseShopState {
	return remember(
		backStack,
		coroutineScope,
		settingRepository
	) {
		DeniseShopState(
			backStack = backStack,
			coroutineScope = coroutineScope,
			userSettingRepository = settingRepository
		)
	}
}
@Stable
class DeniseShopState(
	val backStack: NavBackStack<NavKey>,
	coroutineScope: CoroutineScope,
	userSettingRepository: UserSettingRepository
) {
	val currentRoute: NavKey?
		@Composable get() = backStack.lastOrNull()

	val currentTopLevelRoute: TopLevelRoutes?
		@Composable get() = when (currentRoute) {
			TopLevelRoutes.HOME.route -> TopLevelRoutes.HOME
			TopLevelRoutes.CATEGORY.route -> TopLevelRoutes.CATEGORY
			TopLevelRoutes.WISHLIST.route -> TopLevelRoutes.WISHLIST
			TopLevelRoutes.PROFILE.route -> TopLevelRoutes.PROFILE
			else -> null
		}

	val topLeveRoutes: List<TopLevelRoutes> = TopLevelRoutes.entries

	val isLoggedIn = userSettingRepository.getUser()
		.stateIn(
			coroutineScope,
			SharingStarted.WhileSubscribed(5_000),
			initialValue = null,
		).map { it != null }

	val wishlistItems = userSettingRepository.getWishlistItems()
		.stateIn(
			coroutineScope,
			SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList(),
		)
}