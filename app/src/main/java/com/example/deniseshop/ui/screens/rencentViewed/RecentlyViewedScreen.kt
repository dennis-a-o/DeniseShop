package com.example.deniseshop.ui.screens.rencentViewed

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavOptions
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.deniseshop.R
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.components.ButtonWithProgressIndicator
import com.example.deniseshop.ui.components.items.GridProductItem
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.FooterErrorUI
import com.example.deniseshop.ui.components.common.FooterLoadingUi
import com.example.deniseshop.ui.components.common.LoadingUi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentlyViewedScreen(
	onNavigate: (String, NavOptions?) -> Unit,
	onNavigateUp: () -> Unit,
	viewModel: RecentViewedModel = hiltViewModel(),
){
	val snackBarHostState = remember { SnackbarHostState() }
	val coroutineScope = rememberCoroutineScope()
	val context = LocalContext.current

	val lazyPagingProducts = viewModel.pager.collectAsLazyPagingItems()
	val isLoading by viewModel.isLoading.collectAsState()
	val isError by viewModel.isError.collectAsState()
	val isSuccess by viewModel.isSuccess.collectAsState()
	val  message by viewModel.message.collectAsState()

	LaunchedEffect(isError) {
		if (isError) {
			coroutineScope.launch {
				snackBarHostState.showSnackbar(
					message = message,
					duration = SnackbarDuration.Long,
				)
			}
			viewModel.errorReset()
		}
	}

	LaunchedEffect(isSuccess) {
		if (isSuccess) {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show()
			viewModel.successReset()
		}
	}

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.recentViewed)) },
				modifier = Modifier.shadow(elevation = 1.dp),
				navigationIcon = {
					IconButton(onClick = { onNavigateUp() }) {
						Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "" )
					}
				}
			)
		},
		bottomBar = {
			BottomBar(
				isLoading = isLoading,
				onClearAll = {
				viewModel.onClear()
				}
			)
		},
		snackbarHost = {
			SnackbarHost(hostState = snackBarHostState)
		},
	) { paddingValues ->
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
					LazyVerticalGrid(
						columns = GridCells.Fixed(2),
						contentPadding = PaddingValues(start = 8.dp, bottom = 8.dp),
					) {
						items(lazyPagingProducts.itemCount) {
							lazyPagingProducts[it]?.let { it1 ->
								GridProductItem(
									modifier = Modifier.padding(top = 8.dp, end = 8.dp),
									product = it1,
									onClick = { id ->
										onNavigate("${Routes.ProductDetail.route}/$id",null)
									}
								)
							}
						}
						lazyPagingProducts.apply {
							when (loadState.append) {
								is LoadState.Error -> {
									item(span = { GridItemSpan(2) }) { FooterErrorUI { lazyPagingProducts.retry() } }
								}

								LoadState.Loading -> {
									item(span = { GridItemSpan(2) }) { FooterLoadingUi() }
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
private fun BottomBar(
	modifier: Modifier = Modifier,
	onClearAll: () -> Unit,
	isLoading: Boolean
){
	BottomAppBar(
		modifier = Modifier.shadow(elevation = 8.dp),
		containerColor = MaterialTheme.colorScheme.background
	){
		Row (
			modifier = modifier
				.padding(8.dp)
		){
			ButtonWithProgressIndicator(
				onClick = { onClearAll() },
				isLoading = isLoading,
				modifier = Modifier.fillMaxWidth(),
				shape = RoundedCornerShape(16.dp),
				progressIndicatorModifier = Modifier.scale(0.8f),
			) {
				Text(text = stringResource(R.string.clearALl))
			}
		}
	}
}