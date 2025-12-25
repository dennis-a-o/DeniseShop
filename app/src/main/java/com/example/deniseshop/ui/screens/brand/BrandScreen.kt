package com.example.deniseshop.ui.screens.brand

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavOptions
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.deniseshop.R
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.components.items.BrandItem
import com.example.deniseshop.ui.components.IconWithBadge
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.FooterErrorUI
import com.example.deniseshop.ui.components.common.FooterLoadingUi
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.screens.brand.viewModels.BrandViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandScreen(
	onNavigateUp: () -> Unit,
	onNavigate: (String, NavOptions?) -> Unit,
	cartBadgeCount: Int,
	wishlistBadgeCount: Int,
	viewModel: BrandViewModel = hiltViewModel(),
) {
	val lazyPagingBrands = viewModel.pager.collectAsLazyPagingItems()

	Scaffold(
		modifier = Modifier,
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = stringResource(R.string.brands),
						maxLines = 1,
						overflow = TextOverflow.Ellipsis
					)
				},
				modifier = Modifier.shadow(elevation = 1.dp),
				navigationIcon = {
					IconButton(onClick = { onNavigateUp() }) {
						Icon(painter = painterResource(R.drawable.ic_arrow_back),  contentDescription = "")
					}
				},
				actions = {
					TopAppBarAction(
						cartBadgeCount = cartBadgeCount,
						wishlistBadgeCount = wishlistBadgeCount,
						onClickSearch = {
							onNavigate(Routes.Search.route, null)
						},
						onClickCart = {
							onNavigate(Routes.Cart.route, null)
						},
						onClickWishlist = {
							onNavigate(Routes.Wishlist.route, null)
						}
					)
				}
			)
		}
	){  paddingValues ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
		){
			when (lazyPagingBrands.loadState.refresh) {
				is LoadState.Loading -> {
					LoadingUi()
				}

				is LoadState.Error -> {
					ErrorUi(onErrorAction = { lazyPagingBrands.retry() })
				}

				else -> {
					LazyVerticalGrid(
						columns = GridCells.Fixed(3),
						contentPadding = PaddingValues(start = 8.dp, bottom = 8.dp),
						modifier = Modifier
					) {
						items(lazyPagingBrands.itemCount) {
							lazyPagingBrands[it]?.let { it1 ->
								BrandItem(
									modifier = Modifier.padding(top = 8.dp,end = 8.dp),
									brand = it1,
									onClick = { brandId ->
										onNavigate("${Routes.BrandProductScreen.route}/$brandId/0", null)
									}
								)
							}
						}
						lazyPagingBrands.apply {
							when (loadState.append) {
								is LoadState.Error -> {
									item(span = { GridItemSpan(3) }) { FooterErrorUI { lazyPagingBrands.retry() } }
								}

								LoadState.Loading -> {
									item(span = { GridItemSpan(3) }) { FooterLoadingUi() }
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
private fun TopAppBarAction(
	cartBadgeCount: Int,
	wishlistBadgeCount: Int,
	onClickSearch: () -> Unit,
	onClickCart: () -> Unit,
	onClickWishlist: () -> Unit
) {
	IconButton(
		onClick = { onClickSearch() }
	) {
		Icon(
			painter = painterResource(id = R.drawable.ic_search),
			contentDescription = "",
		)
	}
	IconButton(
		onClick = { onClickWishlist() }
	) {
		IconWithBadge(
			badge = wishlistBadgeCount,
			icon = R.drawable.ic_favorite_outline,
			modifier = Modifier.padding(4.dp),
		)
	}
	IconButton(
		onClick = { onClickCart() }
	) {
		IconWithBadge(
			badge = cartBadgeCount,
			icon = R.drawable.ic_cart_outline,
			modifier = Modifier.padding(4.dp),
		)
	}
}