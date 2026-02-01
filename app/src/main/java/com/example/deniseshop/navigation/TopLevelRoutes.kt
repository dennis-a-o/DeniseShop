package com.example.deniseshop.navigation

import com.example.deniseshop.R

enum class TopLevelRoutes(
	val textId: Int,
	val iconId: Int,
	val iconActiveId: Int,
	val route: Route
){
	HOME(
		textId = R.string.home,
		iconId = R.drawable.ic_home_outline,
		iconActiveId = R.drawable.ic_home_filled,
		route = Route.Home
	),
	CATEGORY(
		textId = R.string.categories,
		iconId = R.drawable.ic_grid_view,
		iconActiveId = R.drawable.ic_grid_view_filled,
		route = Route.Categories
	),
	WISHLIST(
		textId = R.string.wishlist,
		iconId = R.drawable.ic_favorite_outline,
		iconActiveId = R.drawable.ic_favorite_filled,
		route = Route.Wishlists
	),
	PROFILE(
		textId = R.string.profile,
		iconId = R.drawable.ic_account_outline,
		iconActiveId = R.drawable.ic_account_filled,
		route = Route.Profile
	)
}