package com.example.deniseshop.feature.wishlists

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Wishlist
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.IconWithBadge
import com.example.deniseshop.core.presentation.components.LoadingUi
import com.example.deniseshop.navigation.Route
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistsScreen(
	viewModel: WishlistsViewModel = hiltViewModel(),
	onNavigate: (Route) -> Unit,
	onShowSnackBar: suspend (String, String?) -> Boolean
) {
	val wishlistItems = viewModel.wishlistPagingSource.collectAsLazyPagingItems()
	val cartItems = viewModel.cartItems.collectAsState()

	val scope = rememberCoroutineScope()

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		TopAppBar(
			title = { Text(text = stringResource(R.string.wishlist)) },
			modifier = Modifier.shadow(elevation = 1.dp),
			actions = {
				IconButton(
					onClick = { onNavigate(Route.Search) }
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_search),
						contentDescription ="",
					)
				}
				IconButton(
					onClick = { onNavigate(Route.Cart) }
				) {
					IconWithBadge(
						badge = cartItems.value.size,
						icon = R.drawable.ic_cart_outline ,
						modifier = Modifier.padding(4.dp) ,
					)
				}
			}
		)
		PullToRefreshBox(
			isRefreshing = false,
			onRefresh = {
				wishlistItems.refresh()
			},
			modifier = Modifier
				.fillMaxSize()
		) {
			when (wishlistItems.loadState.refresh) {
				 LoadState.Loading -> {
					 LoadingUi(
						 modifier = Modifier
							 .fillMaxSize()
					 )
				 }
				is LoadState.Error -> {
					ErrorUi(
						onErrorAction = {
							wishlistItems.refresh()
						},
					)
				}
				is LoadState.NotLoading -> {
					LazyColumn(
						contentPadding = PaddingValues(8.dp),
						modifier = Modifier,
						verticalArrangement = Arrangement.spacedBy(8.dp)
					) {
						items(wishlistItems.itemCount){ index ->
							wishlistItems[index]?.let {
								WishlistItem(
									wishlist = it,
									onClick = { productId ->
										onNavigate(Route.ProductDetail(productId))
									},
									onCartClick = {},
									onRemoveClick = { id ->
										viewModel.removeWishlist(
											id = id,
											onSuccess = {
												wishlistItems.refresh()
											},
											onError = { error ->
												scope.launch {
													onShowSnackBar(error.toString(), null)
												}
											}
										)
									}
								)
							}
						}

						wishlistItems.apply {
							when (loadState.append) {
								is LoadState.Error -> {
									item {
										ErrorUi(
											onErrorAction = { retry() }
										)
									}
								}
								LoadState.Loading -> {
									item {
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
private fun WishlistItem(
	wishlist: Wishlist,
	onClick: (Long) -> Unit,
	onCartClick: (Long) -> Unit,
	onRemoveClick: (Long) -> Unit,
	modifier: Modifier = Modifier
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable { onClick(wishlist.productId) }
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.Start,
		verticalAlignment = Alignment.Top
	){
		Box {
			if (wishlist.percentageDiscount < 0) {
				Text(
					text = "${String.format(Locale.getDefault(), "%.1f", wishlist.percentageDiscount)}%",
					modifier = Modifier
						.padding(start = 8.dp, top = 8.dp)
						.background(color = Color.Red, shape = RoundedCornerShape(8.dp))
						.padding(horizontal = 8.dp)
						.zIndex(1f),
					style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
				)
			}
			Image(
				painter = rememberAsyncImagePainter(model = wishlist.image),
				contentDescription = "",
				modifier = Modifier
					.width(100.dp)
					.height(118.dp)
					.clip(RoundedCornerShape(14.dp)),
				contentScale = ContentScale.Crop
			)
		}
		Column(modifier = Modifier.padding(8.dp)){
			Text(
				text = wishlist.name,
				style = MaterialTheme.typography.bodyLarge,
				maxLines = 2,
			)
			Row (verticalAlignment = Alignment.CenterVertically){
				Text(
					text = wishlist.activePrice,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					)
				)
				Spacer(Modifier.width(4.dp))
				if(wishlist.percentageDiscount < 0) {
					Text(
						text = wishlist.price,
						style = MaterialTheme.typography.bodySmall.copy(
							color = MaterialTheme.colorScheme.outline,
							textDecoration = TextDecoration.LineThrough
						)
					)
				}
			}
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.CenterVertically
			) {
				IconButton(
					onClick = { onCartClick(wishlist.productId) },
					modifier = Modifier
						.shadow(1.dp, CircleShape)
						.size(32.dp),
					colors = IconButtonDefaults
						.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_cart_filled),
						contentDescription = "",
						modifier = Modifier.size(14.dp),
						tint = MaterialTheme.colorScheme.primary
					)
				}
				Spacer(Modifier.width(8.dp))
				IconButton(
					onClick = { onRemoveClick(wishlist.id) },
					modifier = Modifier
						.shadow(1.dp, CircleShape)
						.size(32.dp),
					colors = IconButtonDefaults
						.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_close),
						contentDescription = "",
						modifier = Modifier.size(14.dp),
						tint = MaterialTheme.colorScheme.primary
					)
				}
			}
		}
	}
}