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
import com.example.deniseshop.core.presentation.components.IconWithBadge
import com.example.deniseshop.core.presentation.theme.DeniseShopTheme
import com.example.deniseshop.navigation.TopLevelRoutes

@Composable
fun DeniseShopNavigationBar(
	topLevelRoutes: List<TopLevelRoutes>,
	currentRoute: TopLevelRoutes?,
	wishlistBadgeCount:Int,
	onRouteClick: (TopLevelRoutes) -> Unit,
){
	NavigationBar(
		modifier = Modifier.shadow(elevation = 8.dp),
		containerColor =  MaterialTheme.colorScheme.background,
	){
		topLevelRoutes.forEach { route ->
			val selected = currentRoute == route
			NavigationBarItem(
				selected = selected,
				onClick = {
					onRouteClick(route)
				},
				icon = {
					if (route == TopLevelRoutes.WISHLIST){
						IconWithBadge(
							badge = wishlistBadgeCount,
							icon = if (selected) route.iconActiveId  else route.iconId,
							modifier = Modifier,
							tint = MaterialTheme.colorScheme.primary
						)
					}else{
						Icon(
							painter = painterResource(id = if (selected) route.iconActiveId  else route.iconId),
							contentDescription = stringResource( route.textId),
							tint = MaterialTheme.colorScheme.primary
						)
					}
				},
				label = {
					Text(
						text = stringResource(route.textId),
						fontWeight = if(selected) FontWeight.Bold else FontWeight.Normal
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
		DeniseShopNavigationBar(
			topLevelRoutes = TopLevelRoutes.entries,
			onRouteClick = { },
			currentRoute = TopLevelRoutes.HOME,
			wishlistBadgeCount = 8
		)
	}
}