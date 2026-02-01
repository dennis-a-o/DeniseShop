package com.example.deniseshop.feature.reviews

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.deniseshop.R
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.LoadingUi
import com.example.deniseshop.core.presentation.components.ReviewItem
import com.example.deniseshop.core.presentation.components.ReviewStatItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(
	viewModel: ReviewsViewModel,
	onBackClick: () -> Unit
) {
	val reviewsItems = viewModel.reviewsPagingSource.collectAsLazyPagingItems()
	val reviewStat by viewModel.reviewStat.collectAsState()


	Column(
		modifier = Modifier
			.windowInsetsPadding(WindowInsets.navigationBars)
			.fillMaxSize()
	) {
		TopAppBar(
			title = {
				Text(
					text =  stringResource(R.string.reviews),
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
			}
		)
		PullToRefreshBox(
			isRefreshing = false,
			onRefresh = {
				reviewsItems.refresh()
			},
			modifier = Modifier
				.fillMaxSize()
		) {
			when (reviewsItems.loadState.refresh) {
				LoadState.Loading ->{
					LoadingUi(
						modifier = Modifier
							.fillMaxSize()
					)
				}
				is LoadState.Error -> {
					ErrorUi(
						onErrorAction = {
							reviewsItems.refresh()
						},
					)
				}
				is LoadState.NotLoading -> {
					LazyVerticalGrid(
						columns = GridCells.Fixed(1),
						contentPadding = PaddingValues(16.dp),
						modifier = Modifier,
						verticalArrangement = Arrangement.spacedBy(16.dp),
					) {
						reviewStat?.let {
							item {
								ReviewStatItem(
									reviewStat = it
								)
							}
						}
						items(reviewsItems.itemCount) { index ->
							reviewsItems[index]?.let {
								ReviewItem(
									review = it,
									modifier = Modifier
										.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
										.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
										.padding(16.dp)
								)
							}
						}

						reviewsItems.apply {
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