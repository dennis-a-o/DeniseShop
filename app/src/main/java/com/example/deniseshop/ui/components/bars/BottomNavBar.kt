package com.example.deniseshop.ui.components.bars

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.deniseshop.ui.components.IconWithBadge
import com.example.deniseshop.R
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.theme.DeniseShopTheme

sealed class BottomNavItem(
	val title: String,
	val icon: Int,
	val iconActive: Int,
	val route: String
){
	data object Home: BottomNavItem(
		title = "Home",
		icon = R.drawable.ic_home_outline,
		iconActive = R.drawable.ic_home_filled,
		route = Routes.Home.route
	)

	data object Category: BottomNavItem(
		title = "Categories",
		icon = R.drawable.ic_grid_view,
		iconActive = R.drawable.ic_grid_view_filled,
		route = Routes.Category.route
	)

	data object Wishlist: BottomNavItem(
		title = "Wishlist",
		icon = R.drawable.ic_favorite_outline,
		iconActive = R.drawable.ic_favorite_filled,
		route = Routes.Wishlist.route
	)

	data object Profile: BottomNavItem(
		title = "Profile",
		icon = R.drawable.ic_account_outline,
		iconActive = R.drawable.ic_account_filled,
		route = Routes.Profile.route
	)
}

@Composable
fun BottomNavBar(
	onNavigate: (String, NavOptions?) -> Unit,
	currentRoute: String,
	wishlistBadgeCount:Int
){
	val items = arrayOf(
		BottomNavItem.Home,
		BottomNavItem.Category,
		BottomNavItem.Wishlist,
		BottomNavItem.Profile,
	)

	NavigationBar(
		modifier = Modifier.shadow(elevation = 8.dp),
		containerColor =  MaterialTheme.colorScheme.background,
	){
		items.forEach { item ->
			val isActive = currentRoute == item.route
			NavigationBarItem(
				selected = isActive,
				onClick = {
					onNavigate(
						item.route,
						NavOptions.Builder().apply {
							setPopUpTo(Routes.Home.route, inclusive = false, saveState = true)
							setLaunchSingleTop(true)
							setRestoreState(true)
						}.build()
					)
		 		},
				icon = {
					if (item == BottomNavItem.Wishlist){
						IconWithBadge(
							badge = wishlistBadgeCount,
							icon = if (isActive) item.iconActive  else item.icon,
							modifier = Modifier,
							tint = MaterialTheme.colorScheme.primary
						)
					}else{
						Icon(
							painter = painterResource(id = if (isActive) item.iconActive  else item.icon),
							contentDescription = item.title,
							tint = MaterialTheme.colorScheme.primary
						)
					}
				},
				label = {
					Text(text = item.title, fontWeight = if(isActive) FontWeight.Bold else FontWeight.Normal)
				}
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun BottomNavBarPreview(){
	DeniseShopTheme {
		BottomNavBar(onNavigate = {_,_-> }, currentRoute = "", wishlistBadgeCount = 8)
	}
}