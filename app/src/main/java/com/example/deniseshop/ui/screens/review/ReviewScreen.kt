package com.example.deniseshop.ui.screens.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.deniseshop.R
import com.example.deniseshop.ui.components.ReviewItem
import com.example.deniseshop.ui.components.ReviewStatItem
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.FooterErrorUI
import com.example.deniseshop.ui.components.common.FooterLoadingUi
import com.example.deniseshop.ui.components.common.LoadingUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
	onNavigateUp: () -> Unit,
	viewModel: ReviewViewModel
){
	val lazyPagingReviews = viewModel.pager.collectAsLazyPagingItems()
	val reviewStat =  viewModel.reviewStat.collectAsState()

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.reviews)) },
				modifier = Modifier.shadow(elevation = 1.dp),
				navigationIcon = {
					IconButton(onClick = { onNavigateUp() }) {
						Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "" )
					}
				}
			)
		},
	){ paddingValues ->
		Box(
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize()
		){
			when (lazyPagingReviews.loadState.refresh) {
				is LoadState.Loading -> {
					LoadingUi()
				}

				is LoadState.Error -> {
					ErrorUi(onErrorAction = { lazyPagingReviews.retry() })
				}

				else -> {
					LazyColumn(
						contentPadding = PaddingValues(8.dp),
						modifier = Modifier
					) {
						item {
							reviewStat.value?.let {
								ReviewStatItem(reviewStat = it)
								Spacer(Modifier.height(8.dp))
								Text(
									text = "${it.totalReview} Reviews",
									style = MaterialTheme.typography.titleMedium
								)
								Spacer(Modifier.height(8.dp))
							}
						}
						items(lazyPagingReviews.itemCount) {
							lazyPagingReviews[it]?.let { it1 ->
								ReviewItem(
									review = it1,
									modifier = Modifier
										.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
										.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
										.padding(8.dp)
								)
								Spacer(Modifier.height(8.dp))
							}
						}
						lazyPagingReviews.apply {
							when (loadState.append) {
								is LoadState.Error -> {
									item { FooterErrorUI { lazyPagingReviews.retry() } }
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

