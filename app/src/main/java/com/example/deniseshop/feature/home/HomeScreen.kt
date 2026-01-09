package com.example.deniseshop.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Home
import com.example.deniseshop.core.presentation.ScreenState
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.feature.home.components.BrandSection
import com.example.deniseshop.feature.home.components.CategorySection
import com.example.deniseshop.feature.home.components.FeaturedSection
import com.example.deniseshop.feature.home.components.FlashSaleSection
import com.example.deniseshop.feature.home.components.NewArrivalSection
import com.example.deniseshop.feature.home.components.RecentViewedSection
import com.example.deniseshop.feature.home.components.SectionSlider
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.components.IconWithBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
	viewModel: HomeViewModel = hiltViewModel(),
	onNavigate: (String) -> Unit
) {
	val state =  viewModel.state.collectAsState()
	val wishlistItems = viewModel.wishlistItems.collectAsState()
	val cartItems = viewModel.cartItems.collectAsState()

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		TopAppBar(
			title = {
				Text(
					text = stringResource(R.string.app_name),
					color = MaterialTheme.colorScheme.primary,
					fontWeight = FontWeight.Bold,
				)
			},
			actions = {
				IconButton(
					onClick = {
						onNavigate(Routes.Search.route)
					}
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_search),
						contentDescription ="",
					)
				}
				IconButton(
					onClick = {
						onNavigate(Routes.Cart.route)
					}
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
			onRefresh = { viewModel.refresh() },
			modifier = Modifier
				.fillMaxSize()
		) {
			when(state.value) {
				is ScreenState.Error -> {
					ErrorUi(
						onErrorAction = {
							viewModel.refresh()
						},
						title = stringResource(R.string.error),
						message = (state.value as ScreenState.Error).error.asString()
					)
				}
				ScreenState.Loading -> {
					CircularProgressIndicator(
						modifier = Modifier
							.align(Alignment.Center)
					)
				}
				is ScreenState.Success<Home> -> {
					HomeScreen(
						home = (state.value as ScreenState.Success<Home>).data,
						wishlistItems = wishlistItems.value,
						cartItems = cartItems.value,
						onCartToggle = { id: Long ->
							viewModel.onCartToggle(id)
						},
						onWishlistToggle = { id: Long ->
							viewModel.onWishlistToggle(id)
						},
						onNavigate = onNavigate
					)
				}
			}
		}
	}
}

@Composable
private fun HomeScreen(
	home: Home,
	wishlistItems: List<Long>,
	cartItems: List<Long>,
	onCartToggle: (Long) -> Unit,
	onWishlistToggle: (Long) -> Unit,
	onNavigate: (String) -> Unit,
	modifier: Modifier = Modifier
){
	LazyColumn (
		modifier = modifier
	){
		if (home.sliders.isNotEmpty()) {
			item {
				SectionSlider(
					sliders = home.sliders,
					onClick = { slider ->
						when (slider.type) {
							"category" -> {
								onNavigate("${Routes.CategoryProductScreen.route}/${slider.typeId}")
							}

							"brand" -> {
								onNavigate("${Routes.BrandProductScreen.route}/${slider.typeId}/0")
							}

							else -> {
								onNavigate("${Routes.ProductScreen.route}/Products")
							}
						}
					}
				)
			}
		}

		if (home.categories.isNotEmpty()){
			item {
				CategorySection(
					categories = home.categories,
					onClick = {
						onNavigate("${Routes.CategoryProductScreen.route}/$it")
					}
				)
			}
		}

		if(home.featuredFlashSale != null){
			item {
				FlashSaleSection(
					featuredFlashSale = home.featuredFlashSale,
					wishlistItems = wishlistItems,
					cartItems = cartItems,
					onSeeAllClick = { id ->
						onNavigate("${Routes.FlashSaleScreen.route}/$id")
					},
					onProductClick = { id ->
						onNavigate("${Routes.ProductDetail.route}/$id")
					},
					onCartToggle = onCartToggle,
					onWishlistToggle = onWishlistToggle
				)
			}
		}
		if (home.featured.isNotEmpty()){
			item {
				FeaturedSection(
					products = home.featured,
					wishlistItems = wishlistItems,
					cartItems = cartItems,
					onProductClick = { id ->
						onNavigate("${Routes.ProductDetail.route}/$id")
					},
					onClickSeeAll = {
						onNavigate( "${Routes.ProductScreen.route}/Featured")
					},
					onCartToggle = onCartToggle,
					onWishlistToggle = onWishlistToggle
				)
			}
		}
		if (home.brands.isNotEmpty()){
			item {
				BrandSection(
					brands = home.brands,
					onClickSeeAll = {
						onNavigate(Routes.BrandScreen.route)
					},
					onBrandClick = {
						onNavigate("${Routes.BrandProductScreen.route}/$it/0")
					}
				)
			}
		}
		if (home.recentViewed.isNotEmpty()){
			item {
				RecentViewedSection(
					products = home.recentViewed,
					wishlistItems = wishlistItems,
					cartItems = cartItems,
					onProductClick = {
						onNavigate("${Routes.ProductDetail.route}/$it")
					},
					onClickSeeAll = {
						onNavigate(Routes.RecentlyViewed.route)
					},
					onCartToggle = onCartToggle,
					onWishlistToggle = onWishlistToggle
				)
			}
		}
		if (home.newArrival.isNotEmpty()){
			item {
				NewArrivalSection(
					products = home.newArrival,
					wishlistItems = wishlistItems,
					cartItems = cartItems,
					onProductClick = {
						onNavigate("${Routes.ProductDetail.route}/$it")
					},
					onSeeAllClick = {
						onNavigate("${Routes.ProductScreen.route}/New arrivals")
					},
					onWishlistToggle = onWishlistToggle,
					onCartToggle = onCartToggle
				)
			}
		}
		item {
			Spacer(Modifier.height(16.dp))
		}
	}
}