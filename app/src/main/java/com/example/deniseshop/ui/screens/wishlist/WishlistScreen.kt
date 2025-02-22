package com.example.deniseshop.ui.screens.wishlist

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavOptions
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.R
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.components.IconWithBadge
import com.example.deniseshop.ui.components.bars.BottomNavBar
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.FooterErrorUI
import com.example.deniseshop.ui.components.common.FooterLoadingUi
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.models.UiWishlist
import com.example.deniseshop.ui.screens.cart.CartViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
	onNavigate: (String, NavOptions?) -> Unit,
	viewModel: WishlistViewModel,
	cartViewModel: CartViewModel
){
	val isLoggedIn = viewModel.isLoggedIn.collectAsState()
	val lazyPagingWishlists = viewModel.pager.collectAsLazyPagingItems()
	val responseState = viewModel.responseState.collectAsState()
	val isRefreshing by viewModel.isRefreshing.collectAsState()
	val wishlistCount = viewModel.wishlistCount.collectAsState()

	val context = LocalContext.current
	val pullState = rememberPullToRefreshState()

	val cartCount = cartViewModel.cartCountState.collectAsState()
	val cartActionState = cartViewModel.actionState.collectAsState()

	LaunchedEffect(responseState.value) {
		if (responseState.value.isError || responseState.value.isSuccess) {
			Toast.makeText(context, responseState.value.message, Toast.LENGTH_LONG).show()
			viewModel.resetResponseState()
		}
	}

	LaunchedEffect(cartActionState.value) {
		if (cartActionState.value.isError || cartActionState.value.isSuccess) {
			Toast.makeText(context, cartActionState.value.message, Toast.LENGTH_LONG).show()
			cartViewModel.resetActionState()
		}
	}

	LaunchedEffect(key1 = isLoggedIn.value) {
		if (!isLoggedIn.value){
			onNavigate(
				Routes.SignInScreen.route,
				NavOptions.Builder().apply {
					setPopUpTo(Routes.Home.route, inclusive = false,saveState = true)
					setLaunchSingleTop(true)
					setRestoreState(true)
				}.build()
			)
		}
	}

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.wishlist)) },
				modifier = Modifier.shadow(elevation = 1.dp),
				actions = {
					TopAppBarAction(
						cartBadgeCountState = cartCount.value,
						onClickSearch = {
							onNavigate(Routes.Search.route,null)
						},
						onClickCart = {
							onNavigate(Routes.Cart.route,null)
						}
					)
				}
			)
		},
		bottomBar = {
			BottomNavBar(
				onNavigate = { route, options -> onNavigate(route, options) },
				currentRoute = Routes.Wishlist.route,
				wishlistBadgeCount = wishlistCount.value
			)
		}
	){ paddingValues ->
		PullToRefreshBox(
			isRefreshing = isRefreshing ,
			onRefresh = {
				viewModel.onRefresh()
			},
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize(),
			state = pullState
		){
			when (lazyPagingWishlists.loadState.refresh) {
				is LoadState.Loading -> {
					LoadingUi()
				}

				is LoadState.Error -> {
					ErrorUi(onErrorAction = { lazyPagingWishlists.retry() })
				}

				else -> {
					LazyColumn(
						contentPadding = PaddingValues(8.dp),
						modifier = Modifier
					) {
						items(lazyPagingWishlists.itemCount) {
							lazyPagingWishlists[it]?.let { it1 ->
								WishlistItem(
									onClick = { id ->
										onNavigate("${Routes.ProductDetail.route}/$id",null)
									},
									onClear = { productId ->
										viewModel.onRemoveWishlist(productId)
										lazyPagingWishlists.refresh()
									},
									onCart = { id -> cartViewModel.onAddCart(id) },
									wishlist = it1
								)
								Spacer(Modifier.height(8.dp))
							}
						}
						lazyPagingWishlists.apply {
							when (loadState.append) {
								is LoadState.Error -> {
									item { FooterErrorUI { lazyPagingWishlists.retry() } }
								}

								LoadState.Loading -> {
									item { FooterLoadingUi() }
								}

								is LoadState.NotLoading -> Unit
							}
						}
					}
				}
			}
		}
	}
}

@Composable
private fun WishlistItem(
	modifier: Modifier = Modifier,
	onClick: (Long) -> Unit,
	onClear: (Long) -> Unit,
	onCart: (Long) -> Unit,
	wishlist: UiWishlist,
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable { onClick(wishlist.productId) }
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.Start,
		verticalAlignment = Alignment.Top
	){
		Box {
			if (wishlist.percentageDiscount < 0) {
				Text(
					text = "${String.format(Locale.getDefault(), "%.1f", wishlist.percentageDiscount)}%",
					modifier = Modifier
						.padding(start = 8.dp, top = 8.dp)
						.background(color = Color.Red, shape = RoundedCornerShape(8.dp))
						.padding(horizontal = 8.dp)
						.zIndex(1f),
					style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
				)
			}
			Image(
				painter = rememberAsyncImagePainter(model = wishlist.image),
				contentDescription = "",
				modifier = Modifier
					.width(100.dp)
					.height(118.dp)
					.clip(RoundedCornerShape(14.dp)),
				contentScale = ContentScale.Crop
			)
		}
		Column(modifier = Modifier.padding(8.dp)){
			Text(
				text = wishlist.name,
				style = MaterialTheme.typography.bodyLarge,
				maxLines = 2,
			)
			Row (verticalAlignment = Alignment.CenterVertically){
				Text(
					text = wishlist.activePrice,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					)
				)
				Spacer(Modifier.width(4.dp))
				if(wishlist.percentageDiscount < 0) {
					Text(
						text = wishlist.price,
						style = MaterialTheme.typography.bodySmall.copy(
							color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
							textDecoration = TextDecoration.LineThrough
						)
					)
				}
			}
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.CenterVertically
			) {
				IconButton(
					onClick = { onCart(wishlist.productId) },
					modifier = Modifier
						.shadow(1.dp, CircleShape)
						.size(32.dp),
					colors = IconButtonDefaults
						.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_cart_filled),
						contentDescription = "",
						modifier = Modifier.size(14.dp),
						tint = MaterialTheme.colorScheme.primary
					)
				}
				Spacer(Modifier.width(8.dp))
				IconButton(
					onClick = { onClear(wishlist.productId) },
					modifier = Modifier
						.shadow(1.dp, CircleShape)
						.size(32.dp),
					colors = IconButtonDefaults
						.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_close),
						contentDescription = "",
						modifier = Modifier.size(14.dp),
						tint = MaterialTheme.colorScheme.primary
					)
				}
			}
		}
	}
}

@Composable
private fun TopAppBarAction(
	cartBadgeCountState: Int,
	onClickSearch: () -> Unit,
	onClickCart: () -> Unit,
){
	IconButton(
		onClick = { onClickSearch() }
	) {
		Icon(
			painter = painterResource(id = R.drawable.ic_search),
			contentDescription ="",
		)
	}
	IconButton(
		onClick = { onClickCart() }
	) {
		IconWithBadge(
			badge = cartBadgeCountState,
			icon = R.drawable.ic_cart_outline ,
			modifier = Modifier.padding(4.dp) ,
		)
	}
}
