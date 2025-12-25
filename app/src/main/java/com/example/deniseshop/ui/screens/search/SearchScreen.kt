package com.example.deniseshop.ui.screens.search

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavOptions
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.deniseshop.R
import com.example.deniseshop.common.event.ProductFilterEvent
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.components.items.GridProductItem
import com.example.deniseshop.ui.components.items.ListProductItem
import com.example.deniseshop.ui.components.bars.ProductConfigBar
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.FooterErrorUI
import com.example.deniseshop.ui.components.common.FooterLoadingUi
import com.example.deniseshop.ui.components.common.FullScreenDialog
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.components.filter.ProductFilter
import com.example.deniseshop.ui.components.filter.ProductSortFilter
import com.example.deniseshop.ui.screens.cart.CartViewModel
import com.example.deniseshop.ui.screens.wishlist.WishlistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
	onNavigate: (String, NavOptions?) -> Unit,
	onNavigateUp: () -> Unit,
	viewModel: SearchViewModel = hiltViewModel(),
	wishlistViewModel: WishlistViewModel,
	cartViewModel: CartViewModel
){
	val isProductFilterVisible by viewModel.isProductFilterVisible.collectAsState()
	val isSortModalVisible by viewModel.isSortModalVisible.collectAsState()
	val selectedSortOption by viewModel.selectedSortOption.collectAsState()
	val filterOptionState by viewModel.filterOptions.collectAsState()
	val filterState by viewModel.filterState.collectAsState()
	val queryHistory by viewModel.queryHistory.collectAsState()
	val lazyPagingProducts = viewModel.pager.collectAsLazyPagingItems()
	val text by viewModel.query.collectAsState()

	var expanded by remember { mutableStateOf(true) }
	var isGrid by rememberSaveable { mutableStateOf(true) }
	var gridCellCount by rememberSaveable { mutableIntStateOf(2) }

	val context = LocalContext.current

	val wishlistItems = wishlistViewModel.wishlistItems.collectAsState()
	val wishlistState = wishlistViewModel.responseState.collectAsState()

	val cartItems = cartViewModel.cartItems.collectAsState()
	val cartActionState = cartViewModel.actionState.collectAsState()

	LaunchedEffect(cartActionState.value) {
		if (cartActionState.value.isError || cartActionState.value.isSuccess) {
			Toast.makeText(context, cartActionState.value.message, Toast.LENGTH_LONG).show()
			cartViewModel.resetActionState()
		}
	}

	LaunchedEffect(wishlistState.value) {
		if (wishlistState.value.isError || wishlistState.value.isSuccess) {
			Toast.makeText(context, wishlistState.value.message, Toast.LENGTH_LONG).show()
			wishlistViewModel.resetResponseState()
		}
	}

	Scaffold(
		topBar = {
			SearchBar(
				inputField = {
					SearchBarDefaults.InputField(
						query = text,
						onQueryChange = { viewModel.onSearchQueryChange(it) },
						onSearch = {
							expanded = false
							lazyPagingProducts.refresh()
						},
						expanded = expanded,
						onExpandedChange = { expanded = it },
						placeholder = {
							Text(
								text = stringResource(R.string.search),
								style = MaterialTheme.typography.bodyLarge.copy(
									color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
								)
							)
						},
						leadingIcon = {
							IconButton(onClick = { onNavigateUp() }) {
								Icon(
									imageVector = Icons.AutoMirrored.Filled.ArrowBack,
									contentDescription = null
								)
							}
						},
						trailingIcon = {
							Row {
								if (text.isNotEmpty()) {
									IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
										Icon(
											imageVector = Icons.Filled.Clear,
											contentDescription = "clear search"
										)
									}
								}
								IconButton(onClick = {
									lazyPagingProducts.refresh()
									expanded = false
								}) {
									Icon(
										imageVector = Icons.Filled.Search,
										contentDescription = "search"
									)
								}
							}
						}
					)
				},
				expanded = expanded,
				onExpandedChange = { expanded = it },
				modifier = Modifier.shadow(elevation = 1.dp , shape = RectangleShape),
				colors = SearchBarDefaults.colors(
					containerColor = MaterialTheme.colorScheme.surface,
					dividerColor = MaterialTheme.colorScheme.surfaceContainer
				),
			) {
				LazyColumn {
					queryHistory.forEach {
						item {
							SearchHistoryItem(
								onClick = {
									viewModel.onSearchQueryChange(it)
									lazyPagingProducts.refresh()
									expanded = false
								},
								onClickRemove = { viewModel.onDeleteSearchHistory(it) },
								query =  it
							)
						}
					}
				}
			}
		},
	){ paddingValues ->
		Box(
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize()
		){
			when (lazyPagingProducts.loadState.refresh) {
				is LoadState.Loading -> {
					LoadingUi()
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
							items(lazyPagingProducts.itemCount) {
								if (isGrid) {
									lazyPagingProducts[it]?.let { it1 ->
										GridProductItem(
											modifier = Modifier.padding(top = 8.dp, end = 8.dp),
											product = it1,
											onClick = { productId ->
												onNavigate("${Routes.ProductDetail.route}/$productId",null)
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
											onClick = { productId ->
												onNavigate("${Routes.ProductDetail.route}/$productId",null)
											},
											onToggleCart = {},
											onToggleWishlist = { id ->
												wishlistViewModel.onToggleWishlist(id)
											},
											wishlistItems = wishlistItems.value,
											cartItems = emptySet()//
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
private fun SearchHistoryItem(
	onClick: (String) -> Unit,
	onClickRemove: (String) -> Unit,
	query: String
){
	Row (
		modifier = Modifier
			.fillMaxWidth()
			.clickable { onClick(query) }
			.padding(8.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row(verticalAlignment = Alignment.CenterVertically) {
			Icon(
				painter = painterResource(R.drawable.ic_history),
				contentDescription = ""
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = query,
				style = MaterialTheme.typography.bodyLarge
			)
		}
		IconButton(
			onClick = { onClickRemove(query) },
			modifier = Modifier.size(20.dp)
		) {
			Icon(
				painter = painterResource(R.drawable.ic_close),
				contentDescription = ""
			)
		}
	}
}