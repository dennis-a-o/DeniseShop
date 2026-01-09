package com.example.deniseshop.core.presentation.designsystem

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.navigation.TopLevelRoutes
import com.example.deniseshop.ui.components.IconWithBadge
import com.example.deniseshop.ui.theme.DeniseShopTheme

@Composable
fun DeniseShopNavigationBar(
	onNavigate: (String, NavOptions?) -> Unit,
	currentRoute: String,
	wishlistBadgeCount:Int
){
	NavigationBar(
		modifier = Modifier.shadow(elevation = 8.dp),
		containerColor =  MaterialTheme.colorScheme.background,
	){
		TopLevelRoutes.entries.forEach { item ->
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
					if (item == TopLevelRoutes.WISHLIST){
						IconWithBadge(
							badge = wishlistBadgeCount,
							icon = if (isActive) item.iconActiveId  else item.iconId,
							modifier = Modifier,
							tint = MaterialTheme.colorScheme.primary
						)
					}else{
						Icon(
							painter = painterResource(id = if (isActive) item.iconActiveId  else item.iconId),
							contentDescription = stringResource( item.textId),
							tint = MaterialTheme.colorScheme.primary
						)
					}
				},
				label = {
					Text(
						text = stringResource(item.textId),
						fontWeight = if(isActive) FontWeight.Bold else FontWeight.Normal
					)
				}
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun DeniseShopNavigationBarPreview(){
	DeniseShopTheme {
		DeniseShopNavigationBar(onNavigate = {_,_-> }, currentRoute = "", wishlistBadgeCount = 8)
	}
}