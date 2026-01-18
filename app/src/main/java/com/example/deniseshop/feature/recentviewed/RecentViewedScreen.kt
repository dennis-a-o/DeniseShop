package com.example.deniseshop.feature.recentviewed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.deniseshop.R
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.GridProductItem
import com.example.deniseshop.core.presentation.components.LoadingUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentViewedScreen(
	viewModel: RecentViewedViewModel = hiltViewModel(),
	onBackClick: () -> Unit,
	onProductClick: (Long) -> Unit,
	onShowSnackBar: suspend (String, String?) -> Boolean
) {
	val state by viewModel.state.collectAsState()
	val productItems = viewModel.recentViewedProductsPagingSource.collectAsLazyPagingItems()

	val cartItems by viewModel.cartItems.collectAsState()
	val wishlistItems by viewModel.wishlistItems.collectAsState()

	var error by remember { mutableStateOf<String?>(null) }

	error = if (state.error != null){
		state.error!!.asString()
	} else null

	LaunchedEffect(error) {
		error?.let {
			onShowSnackBar(it, null)
			viewModel.resetState()
		}
	}

	Column(
		modifier = Modifier
			.windowInsetsPadding(WindowInsets.navigationBars)
			.fillMaxSize()
	) {
		TopAppBar(
			title = {
				Text(
					text =  stringResource(R.string.recentViewed),
					overflow = TextOverflow.Ellipsis,
					maxLines = 1
				)
			},
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
				if (state.isClearing){
					CircularProgressIndicator(
						modifier = Modifier
							.size(20.dp)
					)
				}else {
					IconButton(
						onClick = {
							viewModel.onClear()
						}
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_cleaning_services),
							contentDescription = "",
						)
					}
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
					LazyVerticalGrid(
						columns = GridCells.Fixed(2),
						contentPadding = PaddingValues(8.dp),
						modifier = Modifier,
						verticalArrangement = Arrangement.spacedBy(8.dp),
						horizontalArrangement = Arrangement.spacedBy(8.dp)
					) {
						items(productItems.itemCount) { index ->
							productItems[index]?.let {
								GridProductItem(
									product = it,
									inWishlist = it.id in wishlistItems,
									inCart = it.id in cartItems,
									onClick = onProductClick,
									onCartToggle = { id ->
										viewModel.onCartToggle(id)
									},
									onWishlistToggle = { id ->
										viewModel.onWishlistToggle(id)
									}
								)
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