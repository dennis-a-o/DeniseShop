package com.example.deniseshop.feature.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Order
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.LoadingUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
	viewModel: OrdersViewModel = hiltViewModel(),
	onBackClick: () -> Unit,
	onOrderClick: (Long) -> Unit
) {
	val orderItems = viewModel.ordersPagingSource.collectAsLazyPagingItems()

	Column(
		modifier = Modifier
			.windowInsetsPadding(WindowInsets.navigationBars)
			.fillMaxSize()
	) {
		TopAppBar(
			title = { Text(text = stringResource(R.string.orders)) },
			modifier = Modifier
				.shadow(elevation = 1.dp),
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
				orderItems.refresh()
			},
			modifier = Modifier
				.fillMaxSize()
		) {
			when (orderItems.loadState.refresh) {
				LoadState.Loading ->{
					LoadingUi(
						modifier = Modifier
							.fillMaxSize()
					)
				}
				is LoadState.Error -> {
					ErrorUi(
						onErrorAction = {
							orderItems.refresh()
						},
					)
				}
				is LoadState.NotLoading -> {
					LazyColumn(
						modifier = Modifier
							.fillMaxSize(),
						contentPadding = PaddingValues(16.dp),
						verticalArrangement = Arrangement.spacedBy(16.dp),
					) {
						items(orderItems.itemCount) { index ->
							orderItems[index]?.let {
								OrderItem(
									order = it,
									onOrderClick = onOrderClick
								)
							}
						}

						orderItems.apply {
							when (loadState.append) {
								is LoadState.Error -> {
									item {
										ErrorUi(
											onErrorAction = { retry() }
										)
									}
								}

								LoadState.Loading -> {
									item() {
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

@Composable
private fun OrderItem(
	order: Order,
	onOrderClick: (Long) -> Unit,
	modifier: Modifier = Modifier,
){
	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable { onOrderClick(order.id) }
			.padding(16.dp)
	){
		Row (
			modifier = Modifier.fillMaxWidth()
		){
			AsyncImage(
				model = order.image,
				contentDescription = "",
				modifier = Modifier
					.height(90.dp)
					.width(90.dp)
					.clip(RoundedCornerShape(14.dp)),
				contentScale = ContentScale.Crop
			)
			Spacer(Modifier.width(8.dp))
			Column {
				Text(
					text = order.name,
					style = MaterialTheme.typography.bodyLarge.copy(
						color = MaterialTheme.colorScheme.onBackground
					),
					maxLines = 2
				)
				Row {
					Text(
						text = stringResource(R.string.order),
						style = MaterialTheme.typography.bodyMedium.copy(
							color = MaterialTheme.colorScheme.outline
						),
					)
					Spacer(Modifier.width(8.dp))
					Text(
						text = order.code,
						style = MaterialTheme.typography.bodyMedium,
					)
				}
				Text(
					text = order.status,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					),
				)
				Text(
					text = "On ${order.date.substring(0,10)}",
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.outline
					),
				)
			}
		}
	}
}