package com.example.deniseshop.feature.productdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.ProductDetail
import com.example.deniseshop.core.presentation.ScreenState
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.IconWithBadge
import com.example.deniseshop.core.presentation.components.LoadingUi
import com.example.deniseshop.feature.productdetail.components.AttributesSection
import com.example.deniseshop.feature.productdetail.components.DescriptionSection
import com.example.deniseshop.feature.productdetail.components.DetailsSection
import com.example.deniseshop.feature.productdetail.components.GallerySection
import com.example.deniseshop.feature.productdetail.components.ProductDetailBottomBar
import com.example.deniseshop.feature.productdetail.components.ReviewSection
import com.example.deniseshop.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
	viewModel: ProductDetailViewModel,
	onBackClick: () -> Unit,
	onNavigate: (String) -> Unit,
) {
	val state by viewModel.state.collectAsState()
	val specState by viewModel.specState.collectAsState()
	val cartItems by viewModel.cartItems.collectAsState()
	val wishlistItems by viewModel.wishlistItems.collectAsState()

	Column(
		modifier = Modifier
			.windowInsetsPadding(WindowInsets.navigationBars)
			.fillMaxSize()
	) {
		TopAppBar(
			title = {
				Text(
					text = when(state){
						is ScreenState.Success -> {
							(state as ScreenState.Success<ProductDetail>).data.product.name
						}
						else -> ""
					},
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
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
				IconButton(
					onClick = { onNavigate(Routes.Search.route) }
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_search),
						contentDescription = "",
					)
				}
				IconButton(
					onClick = { onNavigate(Routes.Cart.route) }
				) {
					IconWithBadge(
						badge = cartItems.size,
						icon = R.drawable.ic_cart_outline,
						modifier = Modifier.padding(4.dp),
					)
				}
				IconButton(
					onClick = { onNavigate(Routes.Wishlist.route) }
				) {
					IconWithBadge(
						badge = wishlistItems.size,
						icon = R.drawable.ic_favorite_outline,
						modifier = Modifier.padding(4.dp),
					)
				}
			}
		)
		PullToRefreshBox(
			isRefreshing = false,
			onRefresh = {
				viewModel.onEvent(ProductDetailEvent.Refresh)
			},
			modifier = Modifier
				.fillMaxSize()
		) {
			when(state) {
				is ScreenState.Error -> {
					ErrorUi(
						onErrorAction = {
							viewModel.onEvent(ProductDetailEvent.Retry)
						},
						message = (state as ScreenState.Error).error.asString()
					)
				}
				ScreenState.Loading -> {
					LoadingUi(
						modifier = Modifier
							.fillMaxSize()
					)
				}
				is ScreenState.Success -> {
					ProductDetailScreen(
						productDetail = (state as ScreenState.Success<ProductDetail>).data,
						specState = specState,
						onEvent = {
							viewModel.onEvent(it)
						},
						onNavigate = onNavigate,
					)
				}
			}
		}
	}
}

@Composable
private fun ProductDetailScreen(
	productDetail: ProductDetail,
	specState: ProductDetailSpecState,
	onEvent: (ProductDetailEvent) -> Unit,
	onNavigate: (String) -> Unit,
	modifier: Modifier = Modifier
){
	Column(
		modifier = modifier
			.fillMaxSize()
	) {
		LazyColumn(
			modifier = Modifier
				.weight(1f),
			contentPadding = PaddingValues(16.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			item {
				GallerySection(
					images = productDetail.product.gallery
						.orEmpty()
						.plus(productDetail.product.image)
				)
			}
			item {
				DetailsSection(
					product = productDetail.product,
					selectedQuantity = specState.quantity,
					inWishlist = false,
					inCart = false,
					onEvent = onEvent,
					onClickBrand = {
						onNavigate("${Routes.BrandProductScreen.route}/$it/0")
					},
					onClickCategory = {
						onNavigate("${Routes.CategoryProductScreen.route}/$it")
					}
				)
			}
			item {
				AttributesSection(
					product = productDetail.product,
					selectedColor = specState.color,
					selectedSize = specState.size,
					onEvent = onEvent
				)
			}
			item {
				DescriptionSection(
					summary = productDetail.product.descriptionSummary,
					description = productDetail.product.description,
				)
			}
			item {
				ReviewSection(
					productDetail = productDetail,
					onSeeAllReview = {
						onNavigate("${Routes.Reviews.route}/$it")
					}
				)
			}
		}
		ProductDetailBottomBar(
			isInCart = false,
			onHomeClick = {
				onNavigate(Routes.Home.route)
			},
			onAddToCart = {
				onEvent(ProductDetailEvent.ToggleCart)
			},
			onViewCartClick = {
				onNavigate(Routes.Cart.route)
			}
		)
	}
}