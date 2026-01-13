package com.example.deniseshop.feature.products

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.deniseshop.R
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.GridProductItem
import com.example.deniseshop.core.presentation.components.IconWithBadge
import com.example.deniseshop.core.presentation.components.ListProductItem
import com.example.deniseshop.core.presentation.components.LoadingUi
import com.example.deniseshop.core.presentation.components.ProductConfigBar
import com.example.deniseshop.core.presentation.components.ProductFilterBottomSheet
import com.example.deniseshop.core.presentation.components.ProductSortOptionBottomSheet
import com.example.deniseshop.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
	viewModel: ProductsViewModel = hiltViewModel(),
	title: String = stringResource(R.string.products),
	onBackClick: () -> Unit,
	onNavigate: (String) -> Unit,
) {
	val productItems = viewModel.productPagingSource.collectAsLazyPagingItems()

	val cartItems by viewModel.cartItems.collectAsState()
	val wishlistItems by viewModel.wishlistItems.collectAsState()
	val state by viewModel.state.collectAsState()

	var isGrid by rememberSaveable { mutableStateOf(true) }
	var showProductFilterBottomSheet by rememberSaveable { mutableStateOf(false) }
	var showSortConfigBottomSheet by rememberSaveable { mutableStateOf(false) }

	if (showProductFilterBottomSheet){
		ProductFilterBottomSheet(
			filter = state.filter,
			filterState = state.filterState,
			onApplyFilterState = {
				viewModel.onProductsEvent(ProductsEvent.ProductFilterStateChange(it))
				productItems.refresh()
			},
			onDismiss = {
				showProductFilterBottomSheet = false
			}
		)
	}

	if (showSortConfigBottomSheet){
		ProductSortOptionBottomSheet(
			sortOption = state.sortOption,
			onSortOptionSelect = {
				viewModel.onProductsEvent(ProductsEvent.SortOptionChange(it))
				productItems.refresh()
			},
			onDismiss = {
				showSortConfigBottomSheet = false
			}
		)
	}

	Column(
		modifier = Modifier
			.windowInsetsPadding(WindowInsets.navigationBars)
			.fillMaxSize()
	) {
		TopAppBar(
			title = { Text(text = title) },
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
						badge = cartItems.size,
						icon = R.drawable.ic_cart_outline,
						modifier = Modifier.padding(4.dp),
					)
				}
				IconButton(
					onClick = { onNavigate(Routes.Wishlist.route) }
				) {
					IconWithBadge(
						badge = wishlistItems.size,
						icon = R.drawable.ic_favorite_outline,
						modifier = Modifier.padding(4.dp),
					)
				}
			}
		)
		PullToRefreshBox(
			isRefreshing = false,
			onRefresh = {
				productItems.refresh()
			},
			modifier = Modifier
				.fillMaxSize()
		) {
			when (productItems.loadState.refresh) {
				LoadState.Loading ->{
					LoadingUi(
						modifier = Modifier
							.fillMaxSize()
					)
				}
				is LoadState.Error -> {
					ErrorUi(
						onErrorAction = {
							productItems.refresh()
						},
					)
				}
				is LoadState.NotLoading -> {
					Column(
						modifier = Modifier
							.fillMaxSize()
					) {
						ProductConfigBar(
							isGrid = isGrid,
							onSortClick = {
								showSortConfigBottomSheet = true
							},
							onFilterClick = {
								showProductFilterBottomSheet = true
							},
							onGridListClick = {
								isGrid = !isGrid
							}
						)
						HorizontalDivider(
							thickness = 1.dp,
							color = MaterialTheme.colorScheme.surfaceContainer
						)
						LazyVerticalGrid(
							columns = GridCells.Fixed(
								count = if (isGrid) 2 else 1
							),
							contentPadding = PaddingValues(8.dp),
							modifier = Modifier,
							verticalArrangement = Arrangement.spacedBy(8.dp),
							horizontalArrangement = Arrangement.spacedBy(8.dp)
						) {
							items(productItems.itemCount) { index ->
								productItems[index]?.let {
									if (isGrid){
										GridProductItem(
											product = it,
											inWishlist = it.id in wishlistItems,
											inCart = it.id in cartItems,
											onClick = { productId ->
												onNavigate("${Routes.ProductDetail.route}/$productId")
											},
											onCartToggle = { id ->
												viewModel.onProductsEvent(ProductsEvent.CartToggle(id))
											},
											onWishlistToggle = { id ->
												viewModel.onProductsEvent(ProductsEvent.WishlistToggle(id))
											}
										)
									}else{
										ListProductItem(
											product = it,
											inWishlist = it.id in wishlistItems,
											inCart = it.id in cartItems,
											onClick = { productId ->
												onNavigate("${Routes.ProductDetail.route}/$productId")
											},
											onCartToggle = { id ->
												viewModel.onProductsEvent(ProductsEvent.CartToggle(id))
											},
											onWishlistToggle = { id ->
												viewModel.onProductsEvent(ProductsEvent.WishlistToggle(id))
											}
										)
									}
								}
							}

							productItems.apply {
								when (loadState.append) {
									is LoadState.Error -> {
										item (span = { GridItemSpan(2) }){
											ErrorUi(
												onErrorAction = { retry() }
											)
										}
									}

									LoadState.Loading -> {
										item(span = { GridItemSpan(2) }) {
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