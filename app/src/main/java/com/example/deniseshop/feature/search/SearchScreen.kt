package com.example.deniseshop.feature.search


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.deniseshop.R
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.GridProductItem
import com.example.deniseshop.core.presentation.components.LoadingUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
	viewModel: SearchViewModel = hiltViewModel(),
	onBackClick: () -> Unit,
	onProductClick: (Long) -> Unit,
) {
	val productItems = viewModel.productsPagingSource.collectAsLazyPagingItems()

	val query by viewModel.query.collectAsStateWithLifecycle()
	val searchHistoryQueries by viewModel.queryHistory.collectAsStateWithLifecycle()

	val cartItems  by viewModel.cartItems.collectAsStateWithLifecycle()
	val wishlistItems by viewModel.wishlistItems.collectAsStateWithLifecycle()
	var expanded by remember { mutableStateOf(false) }

	val focusRequester = remember { FocusRequester() }

	LaunchedEffect(Unit){
		focusRequester.requestFocus()
	}

	Column(
		modifier = Modifier
			.windowInsetsPadding(WindowInsets.navigationBars)
			.fillMaxSize()
	) {
		SearchBar(
			inputField = {
				SearchBarDefaults.InputField(
					query = query,
					onQueryChange = {
						viewModel.onQueryChange(it)
					},
					onSearch = {
						expanded = false
						productItems.refresh()
					},
					expanded = expanded,
					onExpandedChange = { expanded = it },
					modifier = Modifier
						.focusRequester(focusRequester)
						.shadow(elevation = 1.dp , shape = RoundedCornerShape(32.dp))
						.background(MaterialTheme.colorScheme.surface),
					placeholder = {
						Text(
							text = stringResource(R.string.search),
							style = MaterialTheme.typography.bodyLarge,
							color = MaterialTheme.colorScheme.outline
						)
					},
					leadingIcon = {
						IconButton(onClick = onBackClick) {
							Icon(
								painter = painterResource(R.drawable.ic_arrow_back),
								contentDescription = null
							)
						}
					},
					trailingIcon = {
						Row {
							if (query.isNotEmpty()) {
								IconButton(
									onClick = {
										viewModel.onQueryChange("")
									}
								) {
									Icon(
										painter = painterResource(R.drawable.ic_close),
										contentDescription = null
									)
								}
							}
							IconButton(
								onClick = {
									productItems.refresh()
									expanded = false
								}
							) {
								Icon(
									painter = painterResource(R.drawable.ic_search),
									contentDescription = null
								)
							}
						}
					}
				)
			},
			expanded = expanded,
			onExpandedChange = { expanded = it },
			modifier = Modifier
				.padding(horizontal = 16.dp),
			colors = SearchBarDefaults.colors(
				containerColor = MaterialTheme.colorScheme.surface,
				dividerColor = Color.Transparent
			),
		) {
			LazyColumn {
				searchHistoryQueries.forEach { searchQuery ->
					item {
						SearchHistoryItem(
							onClick = {
								viewModel.onQueryChange(it)
								productItems.refresh()
								expanded = false
							},
							onClickRemove = { viewModel.clearQuery(it) },
							query = searchQuery
						)
					}
				}
			}
		}

		Box(
			modifier = Modifier
				.fillMaxSize()
		) {
			when (productItems.loadState.refresh) {
				LoadState.Loading -> {
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
					if (productItems.itemCount == 0) {
						Text(
							text = stringResource(R.string.no_results),
							modifier = Modifier
								.align(Alignment.Center),
							style = MaterialTheme.typography.titleLarge
						)
					} else {
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
										onClick = { productId ->
											onProductClick(productId)
										},
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
										item(span = { GridItemSpan(2) }) {
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