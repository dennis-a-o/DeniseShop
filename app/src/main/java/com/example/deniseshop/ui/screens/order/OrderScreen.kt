package com.example.deniseshop.ui.screens.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavOptions
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.R
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.FooterErrorUI
import com.example.deniseshop.ui.components.common.FooterLoadingUi
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.models.UiOrder
import com.example.deniseshop.ui.screens.order.viewModels.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
	onNavigate: (String, NavOptions?) -> Unit,
	onNavigateUp: () -> Unit,
	viewModel: OrderViewModel = hiltViewModel()
){
	val lazyPagingOrders = viewModel.pager.collectAsLazyPagingItems()

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.orders)) },
				modifier = Modifier.shadow(elevation = 1.dp),
				navigationIcon = {
					IconButton(onClick = { onNavigateUp() }) {
						Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = "" )
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
			when (lazyPagingOrders.loadState.refresh) {
				is LoadState.Loading -> {
					LoadingUi()
				}

				is LoadState.Error -> {
					ErrorUi(onErrorAction = { lazyPagingOrders.retry() })
				}

				else -> {
					LazyColumn(
						contentPadding = PaddingValues(8.dp)
					) {
						items(lazyPagingOrders.itemCount){
							lazyPagingOrders[it]?.let { it1 ->
								OrderItem(
									order = it1,
									onOrderClick = { id ->
										onNavigate("${Routes.OrderDetail.route}/$id",null)
									}
								)
								Spacer(Modifier.height(8.dp))
							}
						}
						lazyPagingOrders.apply {
							when (loadState.append) {
								is LoadState.Error -> {
									item { FooterErrorUI { lazyPagingOrders.retry() } }
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

@Composable
private fun OrderItem(
	modifier: Modifier = Modifier,
	order: UiOrder,
	onOrderClick: (Long) -> Unit
){
	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable { onOrderClick(order.id) }
			.padding(8.dp)
	){
		Row (
			modifier = Modifier.fillMaxWidth()
		){
			Image(
				painter = rememberAsyncImagePainter(model = order.image),
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
						text = "Order",
						style = MaterialTheme.typography.bodyMedium.copy(
							color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
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
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
					),
				)
			}
		}
	}
}