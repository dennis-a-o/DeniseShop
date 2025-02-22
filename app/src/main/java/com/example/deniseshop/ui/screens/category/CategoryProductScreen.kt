package com.example.deniseshop.ui.screens.category

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.deniseshop.R
import com.example.deniseshop.common.event.ProductFilterEvent
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.components.BrandItem
import com.example.deniseshop.ui.components.CategoryItem
import com.example.deniseshop.ui.components.GridProductItem
import com.example.deniseshop.ui.components.IconWithBadge
import com.example.deniseshop.ui.components.ListProductItem
import com.example.deniseshop.ui.components.bars.ProductConfigBar
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.FooterErrorUI
import com.example.deniseshop.ui.components.common.FooterLoadingUi
import com.example.deniseshop.ui.components.common.FullScreenDialog
import com.example.deniseshop.ui.components.filter.ProductFilter
import com.example.deniseshop.ui.components.filter.ProductSortFilter
import com.example.deniseshop.ui.models.UiBrand
import com.example.deniseshop.ui.models.UiCategory
import com.example.deniseshop.ui.screens.cart.CartViewModel
import com.example.deniseshop.ui.screens.category.viewModels.CategoryProductViewModel
import com.example.deniseshop.ui.screens.wishlist.WishlistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryProductScreen(
	onNavigate: (String, NavOptions?) -> Unit,
	onNavigateUp: () -> Unit,
	viewModel: CategoryProductViewModel,
	wishlistViewModel: WishlistViewModel,
	cartViewModel: CartViewModel
) {
	val title by viewModel.title.collectAsState()
	val isProductFilterVisible by viewModel.isProductFilterVisible.collectAsState()
	val isSortModalVisible by viewModel.isSortModalVisible.collectAsState()
	val selectedSortOption by viewModel.selectedSortOption.collectAsState()
	val filterOptionState by viewModel.filterOptions.collectAsState()
	val filterState by viewModel.filterState.collectAsState()
	val lazyPagingProducts = viewModel.pager.collectAsLazyPagingItems()
	val category by viewModel.category.collectAsState()

	var isRefreshing by remember { mutableStateOf(false) }
	var isGrid by rememberSaveable { mutableStateOf(true) }
	var gridCellCount by rememberSaveable { mutableIntStateOf(2) }

	val pullState = rememberPullToRefreshState()
	val context = LocalContext.current

	val wishlistCount = wishlistViewModel.wishlistCount.collectAsState()
	val wishlistItems = wishlistViewModel.wishlistItems.collectAsState()
	val wishlistState = wishlistViewModel.responseState.collectAsState()

	val cartCount = cartViewModel.cartCountState.collectAsState()
	val cartItems = cartViewModel.cartItems.collectAsState()
	val cartState = cartViewModel.actionState.collectAsState()

	isRefreshing = lazyPagingProducts.loadState.refresh is LoadState.Loading

	LaunchedEffect(wishlistState.value) {
		if (wishlistState.value.isError || wishlistState.value.isSuccess) {
			Toast.makeText(context, wishlistState.value.message, Toast.LENGTH_LONG).show()
			wishlistViewModel.resetResponseState()
		}
	}

	LaunchedEffect(cartState.value) {
		if (cartState.value.isError || cartState.value.isSuccess) {
			Toast.makeText(context,cartState.value.message, Toast.LENGTH_LONG).show()
			cartViewModel.resetActionState()
		}
	}

	Scaffold(
		modifier = Modifier,
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = title,
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
						cartBadgeCount = cartCount.value,
						wishlistBadgeCount = wishlistCount.value,
						onClickSearch = {
							onNavigate(Routes.Search.route,null)
						},
						onClickCart = {
							onNavigate(Routes.Cart.route,null)
						},
						onClickWishlist = {
							onNavigate(Routes.Wishlist.route, null)
						}
					)
				}
			)
		}
	){  paddingValues ->
		PullToRefreshBox(
			isRefreshing = isRefreshing ,
			onRefresh = {
				lazyPagingProducts.refresh()
			},
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize(),
			state = pullState
		){
			when (lazyPagingProducts.loadState.refresh) {
				is LoadState.Loading -> {
					//LoadingUi()
				}

				is LoadState.Error -> {
					ErrorUi(onErrorAction = { lazyPagingProducts.retry() })
				}

				else -> {
					Column (modifier = Modifier.fillMaxSize()) {
						ProductConfigBar(
							onClickLayout = {
								isGrid = !isGrid
								gridCellCount = if (isGrid) 2 else 1
							},
							onClickSort = { viewModel.onOpenSortModal() },
							onClickFilter = { viewModel.onProductFilterEvent(ProductFilterEvent.Open) },
							isGrid = isGrid
						)
						HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.surfaceContainer)
						LazyVerticalGrid(
							columns = GridCells.Fixed(gridCellCount),
							contentPadding = PaddingValues(start = 8.dp, bottom = 8.dp),
							modifier = Modifier
						) {
							category?.categories?.let { categories ->
								if(categories.isNotEmpty()) {
									item(span = { GridItemSpan(gridCellCount) }) {
										RowCategoryList(
											categories = categories,
											onClick = { id ->
												onNavigate("${Routes.CategoryProductScreen.route}/$id",null)
											}
										)
									}
								}
							}

							category?.brands?.let { brands ->
								if (brands.isNotEmpty()) {
									item(span = { GridItemSpan(gridCellCount) }) {
										RowBrandList(
											brands = brands,
											onClick = { id ->
												onNavigate("${Routes.BrandProductScreen.route}/$id/0",null)
											}
										)
									}
								}
							}

							items(lazyPagingProducts.itemCount) {
								if (isGrid) {
									lazyPagingProducts[it]?.let { it1 ->
										GridProductItem(
											modifier = Modifier.padding(top = 8.dp, end = 8.dp),
											product = it1,
											onClick = { id ->
												onNavigate("${Routes.ProductDetail.route}/$id",null)
											},
											onToggleCart = { id -> cartViewModel.onToggleCart(id) },
											onToggleWishlist = { id ->
												wishlistViewModel.onToggleWishlist(id)
											},
											wishlistItems = wishlistItems.value,
											cartItems = cartItems.value
										)
									}
								} else {
									lazyPagingProducts[it]?.let { it1 ->
										ListProductItem(
											modifier = Modifier.padding(top = 8.dp, end = 8.dp),
											product = it1,
											onClick = { id ->
												onNavigate("${Routes.ProductDetail.route}/$id",null)
											},
											onToggleCart = { id -> cartViewModel.onToggleCart(id) },
											onToggleWishlist = { id ->
												wishlistViewModel.onToggleWishlist(id)
											},
											wishlistItems = wishlistItems.value,
											cartItems = cartItems.value
										)
									}
								}
							}
							lazyPagingProducts.apply {
								when (loadState.append) {
									is LoadState.Error -> {
										item(span = { GridItemSpan(gridCellCount) }) { FooterErrorUI { lazyPagingProducts.retry() } }
									}

									LoadState.Loading -> {
										item(span = { GridItemSpan(gridCellCount) }) { FooterLoadingUi() }
									}

									is LoadState.NotLoading -> Unit
								}
							}
						}
					}
				}
			}
		}

		if(isProductFilterVisible){
			FullScreenDialog(
				onDismiss = { viewModel.onProductFilterEvent(ProductFilterEvent.Close) }
			) {
				ProductFilter(
					filterOptionState = filterOptionState,
					filterState = filterState,
					onFilterEvent = {
						viewModel.onProductFilterEvent(it)

						if (it is ProductFilterEvent.Apply){
							lazyPagingProducts.refresh()
						}
					},
					onClose = { viewModel.onProductFilterEvent(ProductFilterEvent.Close) }
				)
			}
		}

		if (isSortModalVisible){
			ModalBottomSheet(onDismissRequest = { viewModel.onDismissSortModal() }) {
				ProductSortFilter(
					onSelectOption = {
						viewModel.onSortOptionSelected(it)
						viewModel.onDismissSortModal()
						lazyPagingProducts.refresh()
					},
					onDismiss = { viewModel.onDismissSortModal() },
					selectedOption = selectedSortOption
				)
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

@Composable
private fun RowCategoryList(
	categories: List<UiCategory>,
	onClick: (Long) -> Unit
){
	Column {
		Spacer(Modifier.height(8.dp))
		LazyRow {
			categories.forEach{ category ->
				item {
					CategoryItem(
						modifier = Modifier.width(80.dp),
						category = category,
						onClick = { onClick(it) }
					)
					Spacer(Modifier.width(8.dp))
				}
			}
		}
		Spacer(Modifier.height(8.dp))
	}
}

@Composable
private fun RowBrandList(
	brands: List<UiBrand>,
	onClick: (Long) -> Unit
){
	Column {
		Spacer(Modifier.height(8.dp))
		Text(
			text = stringResource(R.string.brands),
			style = MaterialTheme.typography.titleMedium
		)
		Spacer(Modifier.height(8.dp))
		LazyRow{
			brands.forEach { brand ->
				item {
					BrandItem(
						modifier = Modifier.width(80.dp),
						brand = brand,
						onClick = { onClick(it) }
					)
					Spacer(Modifier.width(8.dp))
				}
			}
		}
		Spacer(Modifier.height(8.dp))
	}
}
