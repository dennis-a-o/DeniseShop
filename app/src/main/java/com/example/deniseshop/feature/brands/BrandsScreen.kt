package com.example.deniseshop.feature.brands

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.deniseshop.R
import com.example.deniseshop.core.presentation.components.BrandItem
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.IconWithBadge
import com.example.deniseshop.core.presentation.components.LoadingUi
import com.example.deniseshop.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandsScreen(
	viewModel: BrandsViewModel = hiltViewModel(),
	onBackClick: () -> Unit,
	onNavigate: (String) -> Unit
) {
	val brandItems = viewModel.brandsPagingSource.collectAsLazyPagingItems()

	val cartItemsCount by viewModel.cartItemsCount.collectAsState(0)
	val wishlistItemsCount by viewModel.wishlistItemsCount.collectAsState(0)

	Column(
		modifier = Modifier
			.windowInsetsPadding(WindowInsets.navigationBars)
			.fillMaxSize()
	) {
		TopAppBar(
			title = { Text(text = stringResource(R.string.brands)) },
			modifier = Modifier.shadow(elevation = 1.dp),
			navigationIcon = {
				IconButton(
					onClick = onBackClick
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_arrow_back),
						contentDescription = "",
					)
				}
			},
			actions = {
				IconButton(
					onClick = { onNavigate(Routes.Search.route) }
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_search),
						contentDescription = "",
					)
				}
				IconButton(
					onClick = { onNavigate(Routes.Cart.route) }
				) {
					IconWithBadge(
						badge = cartItemsCount,
						icon = R.drawable.ic_cart_outline,
						modifier = Modifier.padding(4.dp),
					)
				}
				IconButton(
					onClick = { onNavigate(Routes.Wishlist.route) }
				) {
					IconWithBadge(
						badge = wishlistItemsCount,
						icon = R.drawable.ic_favorite_outline,
						modifier = Modifier.padding(4.dp),
					)
				}
			}
		)
		PullToRefreshBox(
			isRefreshing = false,
			onRefresh = {
				brandItems.refresh()
			},
			modifier = Modifier
				.fillMaxSize()
		) {
			when (brandItems.loadState.refresh) {
				LoadState.Loading ->{
					LoadingUi(
						modifier = Modifier
							.fillMaxSize()
					)
				}
				is LoadState.Error -> {
					ErrorUi(
						onErrorAction = {
							brandItems.refresh()
						},
					)
				}
				is LoadState.NotLoading -> {
					Column(
						modifier = Modifier
							.fillMaxSize()
					) {
						LazyVerticalGrid(
							columns = GridCells.Fixed(count = 3),
							contentPadding = PaddingValues(8.dp),
							modifier = Modifier,
							verticalArrangement = Arrangement.spacedBy(8.dp),
							horizontalArrangement = Arrangement.spacedBy(8.dp)
						) {
							items(brandItems.itemCount) { index ->
								brandItems[index]?.let {
									BrandItem(
										brand = it,
										onClick = { id ->
											onNavigate("${Routes.BrandProductScreen.route}/$id/0")
										}
									)
								}
							}

							brandItems.apply {
								when (loadState.append) {
									is LoadState.Error -> {
										item (span = { GridItemSpan(3) }){
											ErrorUi(
												onErrorAction = { retry() }
											)
										}
									}

									LoadState.Loading -> {
										item(span = { GridItemSpan(3) }) {
											LoadingUi()
										}
									}

									is LoadState.NotLoading -> {}
								}
							}
						}
					}
				}
			}
		}
	}
}