package com.example.deniseshop.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class Navigator(
	private val backStack: NavBackStack<NavKey>,
	private val onNavigateToRestrictedKey: (targetKey: Route) -> Route,
	private val isLoggedIn: () -> Boolean,
) {
	fun navigate(key: Route) {
		if (key.requireLogin && !isLoggedIn()) {
			val loginKey = onNavigateToRestrictedKey(key)
			backStack.add(loginKey)
		} else {
			backStack.add(key)
		}
	}

	fun goBack() = backStack.removeLastOrNull()

	fun navigateHome(){
		backStack.clear()
		backStack.add(Route.Home)
	}

	fun navigateTopLevel(key: Route){
		//ensure singleTop for TopLevelRoutes work around
		if (backStack.contains(key)){
			if (key == Route.Home){
				backStack.clear()
			}else {
				backStack.remove(key)
			}
		}

		this.navigate(key)
	}
}