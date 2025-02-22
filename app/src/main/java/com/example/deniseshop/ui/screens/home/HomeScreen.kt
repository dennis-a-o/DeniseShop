package com.example.deniseshop.ui.screens.home

import android.provider.Contacts.Intents.UI
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.collectIsDraggedAsState
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.R
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.ui.components.bars.BottomNavBar
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.components.BrandItem
import com.example.deniseshop.ui.components.ColumnGrid
import com.example.deniseshop.ui.components.CountDownType
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.FlashSaleCountDown
import com.example.deniseshop.ui.components.GridProductItem
import com.example.deniseshop.ui.components.IconWithBadge
import com.example.deniseshop.ui.components.LazyRowGrid
import com.example.deniseshop.ui.components.ListProductItem
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.models.UiBrand
import com.example.deniseshop.ui.models.UiCategory
import com.example.deniseshop.ui.models.UiFeaturedFlashSale
import com.example.deniseshop.ui.models.UiHome
import com.example.deniseshop.ui.models.UiProduct
import com.example.deniseshop.ui.models.UiSlider
import com.example.deniseshop.ui.screens.cart.CartViewModel
import com.example.deniseshop.ui.screens.wishlist.WishlistViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
	onNavigate: (String,NavOptions?) -> Unit,
	wishlistViewModel: WishlistViewModel,
	cartViewModel: CartViewModel,
	viewModel: HomeViewModel = hiltViewModel()
) {
	val homeState by viewModel.homeState.collectAsState()
	val isRefreshing by viewModel.isRefreshing.collectAsState()

	val pullState = rememberPullToRefreshState()
	val context = LocalContext.current

	val wishlistCount = wishlistViewModel.wishlistCount.collectAsState()
	val wishlistItems = wishlistViewModel.wishlistItems.collectAsState()
	val wishlistState = wishlistViewModel.responseState.collectAsState()

	val cartCount = cartViewModel.cartCountState.collectAsState()
	val cartItems = cartViewModel.cartItems.collectAsState()
	val cartState = cartViewModel.actionState.collectAsState()

	LaunchedEffect(
		key1 = wishlistState.value,
		key2 = cartState.value
	) {
		if (wishlistState.value.isError || wishlistState.value.isSuccess) {
			Toast.makeText(context, wishlistState.value.message, Toast.LENGTH_LONG).show()
			wishlistViewModel.resetResponseState()
		}
		if (cartState.value.isError || cartState.value.isSuccess) {
			Toast.makeText(context,cartState.value.message, Toast.LENGTH_LONG).show()
			cartViewModel.resetActionState()
		}
	}

	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = stringResource(id = R.string.app_name),
						color = MaterialTheme.colorScheme.primary,
						fontWeight = FontWeight.Bold
					)
				},
				modifier = Modifier.shadow(elevation = 1.dp),
				actions = {
					HomeTopAppBarAction(
						onClickSearch =  {
							onNavigate(Routes.Search.route, null)
						},
						onClickCart = {
							onNavigate(Routes.Cart.route, null)
						},
						cartBadgeCountState = cartCount.value
					)
				}
			)
		},
		bottomBar = {
			BottomNavBar(
				onNavigate = {rt, opt -> onNavigate(rt, opt) },
				currentRoute = Routes.Home.route,
				wishlistBadgeCount = wishlistCount.value
			)
		}
	){ paddingValues ->
		PullToRefreshBox(
			isRefreshing = isRefreshing ,
			onRefresh = {
				viewModel.onRefresh()
			},
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize(),
			state = pullState
		) {
			when(homeState){
				is ScreenState.Error -> {
					ErrorUi(onErrorAction = { viewModel.onRetry() })
				}
				is ScreenState.Loading -> {
					LoadingUi()
				}
				is ScreenState.Success -> {
					HomeScreen(
						onNavigate = { route, option -> onNavigate(route, option) },
						homeStateData = (homeState as ScreenState.Success<UiHome>).uiData,
						onToggleCart = {
							cartViewModel.onToggleCart(it)
						},
						onToggleWishlist = {
							wishlistViewModel.onToggleWishlist(it)
						},
						wishlistItems = wishlistItems.value,
						cartItems = cartItems.value
					)
				}
			}
		}
	}
}

@Composable
private fun HomeScreen(
	modifier: Modifier = Modifier,
	onNavigate: (String, NavOptions?) -> Unit,
	homeStateData: UiHome,
	onToggleCart: (Long) -> Unit,
	onToggleWishlist: (Long) -> Unit,
	wishlistItems: Set<Long>,
	cartItems: Set<Long>
){
	LazyColumn (
		modifier = modifier
	){
		item {
			if (homeStateData.sliders.isNotEmpty()) {
				HomeSlider(
					sliders = homeStateData.sliders,
					onClick = {slider ->
						when(slider.type){
							"category" -> {
								onNavigate("${Routes.CategoryProductScreen.route}/${slider.typeId}", null)
							}
							"brand" -> {
								onNavigate("${Routes.BrandProductScreen.route}/${slider.typeId}/0", null)
							}
							else -> {
								onNavigate( "${Routes.ProductScreen.route}/Products",null)
							}
						}
					}
				)
			}
		}
		item {
			if(homeStateData.categories.isNotEmpty()) {
				Spacer(Modifier.height(16.dp))
				HomeCategory(
					onClick = { onNavigate("${Routes.CategoryProductScreen.route}/$it", null) },
					categories = homeStateData.categories
				)
			}
		}
		item{
			homeStateData.featuredFlashSale?.let {
				Spacer(Modifier.height(16.dp))
				HomeFlashSale(
					onProductClick = { id ->
						onNavigate("${Routes.ProductDetail.route}/$id",null)
					},
					onClickSeeAll = { id ->
						onNavigate("${Routes.FlashSaleScreen.route}/$id",null)
					},
					featuredFlashSale = homeStateData.featuredFlashSale,
					onToggleCart = { onToggleCart(it) },
					onToggleWishlist = { onToggleWishlist(it) },
					wishlistItems = wishlistItems,
					cartItems = cartItems
				)
			}
		}
		item{
			if (homeStateData.featured.isNotEmpty()) {
				Spacer(Modifier.height(16.dp))
				HomeFeatured(
					onClickSeeAll = {
						onNavigate( "${Routes.ProductScreen.route}/Featured",null)
					},
					onClickItem = { id ->
						onNavigate("${Routes.ProductDetail.route}/$id",null)
					},
					products = homeStateData.featured,
					onToggleCart = { onToggleCart(it) },
					onToggleWishlist = { onToggleWishlist(it) },
					wishlistItems = wishlistItems,
					cartItems = cartItems
				)
			}
		}
		item{
			if(homeStateData.brands .isNotEmpty()) {
				Spacer(Modifier.height(16.dp))
				HomeFeaturedBrand(
					onClickSeeAll = {
						onNavigate(Routes.BrandScreen.route, null)
					},
					onClickItem = {
						onNavigate("${Routes.BrandProductScreen.route}/$it/0",null)
					},
					brands = homeStateData.brands
				)
			}
		}
		item {
			if (homeStateData.recentViewed.isNotEmpty()) {
				Spacer(Modifier.height(16.dp))
				HomeRecentViewed(
					onClick = {
						onNavigate("${Routes.ProductDetail.route}/$it",null)
					},
					onClickSeeAll = {
						onNavigate(Routes.RecentlyViewed.route,null)
					},
					products = homeStateData.recentViewed,
					onToggleCart = { onToggleCart(it) },
					onToggleWishlist = { onToggleWishlist(it) },
					wishlistItems = wishlistItems,
					cartItems = cartItems
				)
			}
		}
		item{
			if (homeStateData.newArrival.isNotEmpty()) {
				Spacer(Modifier.height(16.dp))
				HomeNewArrival(
					onClickSeeAll = {
						onNavigate("${Routes.ProductScreen.route}/New arrivals", null)
					},
					onClickItem = {
						onNavigate("${Routes.ProductDetail.route}/$it",null)
					},
					products = homeStateData.newArrival,
					onToggleCart = { onToggleCart(it) },
					onToggleWishlist = { onToggleWishlist(it) },
					wishlistItems = wishlistItems,
					cartItems = cartItems
				)
				Spacer(Modifier.height(16.dp))
			}
		}
	}
}

@Composable
private fun HomeSlider(
	sliders: List<UiSlider>,
	onClick: (UiSlider) -> Unit,
	pagerState: PagerState = rememberPagerState(
		initialPage = 0,
		initialPageOffsetFraction = 0f,
		pageCount = { sliders.size }
	)
){
	val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
	if (isDragged.not()){
		with(pagerState){
			var currentPagerKey by remember { mutableIntStateOf(0) }
			LaunchedEffect(key1 = currentPagerKey) {
				launch {
					delay(5000L)
					val nextPage = (currentPage+1).mod(pageCount)
					animateScrollToPage(nextPage)
					currentPagerKey = nextPage
				}
			}
		}
	}

	Box {
		HorizontalPager(
			state = pagerState,
			pageSize = PageSize.Fill,
			contentPadding = PaddingValues(16.dp),
			pageSpacing = 16.dp
		) { page ->
			BannerItem(slider = sliders[page], onClick = { onClick(it) } )
		}
		DotIndicators(
			pagerState = pagerState,
			modifier = Modifier.align(Alignment.BottomCenter)
		)
	}
}

@Composable
private fun HomeCategory(
	modifier: Modifier = Modifier,
	onClick: (Long) -> Unit,
	categories: List<UiCategory>
){
	Column (modifier){
		Row (modifier = Modifier.padding(horizontal = 16.dp)){
			Text(
				text = stringResource(R.string.categories),
				style = MaterialTheme.typography.titleMedium,
				maxLines = 1
			)
		}
		Spacer(Modifier.height(16.dp))
		Row(
			modifier = Modifier
				.horizontalScroll(
					rememberScrollState()
				)
		) {
			Spacer(Modifier.width(16.dp))
			Button(
				onClick = {},
				modifier = Modifier
			) {
				Text(
					text = stringResource(R.string.all),
					style = MaterialTheme.typography.bodyMedium
				)
			}
			Spacer(modifier = Modifier.width(4.dp))
			categories.forEach {
				OutlinedButton(
					onClick = { onClick(it.id) },
					border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
				) {
					Text(
						text = it.name,
						style = MaterialTheme.typography.bodyMedium
					)
				}
				Spacer(modifier = Modifier.width(4.dp))
			}
			Spacer(Modifier.width(12.dp))
		}
	}
}

@Composable
private fun HomeFlashSale(
	modifier: Modifier = Modifier,
	onProductClick: (Long) -> Unit,
	onClickSeeAll: (Long) -> Unit,
	onToggleCart: (Long) -> Unit,
	onToggleWishlist: (Long) -> Unit,
	featuredFlashSale: UiFeaturedFlashSale,
	wishlistItems: Set<Long>,
	cartItems: Set<Long>
){
	Column(modifier) {
		Row(
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.flashSales),
				style = MaterialTheme.typography.titleMedium
			)
			TextButton(
				onClick = { onClickSeeAll(featuredFlashSale.flashSale.id) },
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				Text(
					text = stringResource(R.string.seeAll),
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
		Spacer(Modifier.height(16.dp))
		Row (
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(horizontal = 16.dp)
		){
			Text(
				text = stringResource(R.string.timeLeft),
				style = MaterialTheme.typography.bodySmall.copy(
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
				)
			)
			Spacer(Modifier.width(4.dp))
			FlashSaleCountDown(
				endDate = featuredFlashSale.flashSale.endDate,
				countDownType = CountDownType.DD_HH_MM_SS
			)
		}
		Spacer(Modifier.height(16.dp))
		Row (Modifier.horizontalScroll(rememberScrollState())){
			Spacer(Modifier.width(16.dp))
			featuredFlashSale.products.forEach { product ->
				GridProductItem(
					modifier = Modifier.width(170.dp),
					onClick = { onProductClick(product.id) },
					product = product,
					onToggleWishlist = { onToggleWishlist(it) },
					onToggleCart = { onToggleCart(it) },
					wishlistItems = wishlistItems,
					cartItems = cartItems
				)
				Spacer(Modifier.width(8.dp))
			}
			Spacer(Modifier.width(8.dp))
		}
	}
}

@Composable
private fun HomeFeatured(
	modifier: Modifier = Modifier,
	onClickSeeAll: () -> Unit,
	onClickItem: (Long) -> Unit,
	products: List<UiProduct>,
	onToggleWishlist: (Long) -> Unit,
	onToggleCart: (Long) -> Unit,
	wishlistItems: Set<Long>,
	cartItems:  Set<Long>
){
	Column (modifier = modifier){
		Row(
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.featuredProducts),
				style = MaterialTheme.typography.titleMedium
			)
			TextButton(
				onClick = { onClickSeeAll() },
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				Text(
					text = stringResource(R.string.seeAll),
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
		Spacer(Modifier.height(16.dp))
		LazyRowGrid(
			rows = 2,
			itemCount = products.size,
			gridWidth = 320.dp,
			gridWidthSpacing = 8.dp,
			gridHeightSpacing = 8.dp,
			gridHorizontalPadding = 16.dp
		){ index ->
			ListProductItem(
				onClick = { productId -> onClickItem(productId) },
				product = products[index],
				onToggleCart = { onToggleCart(it) },
				onToggleWishlist = { onToggleWishlist(it) },
				wishlistItems = wishlistItems,
				cartItems = cartItems
			)
		}
	}
}
@Composable
private fun HomeFeaturedBrand(
	modifier: Modifier = Modifier,
	onClickSeeAll: () -> Unit,
	onClickItem: (Long) -> Unit,
	brands: List<UiBrand>,
){
	Column (modifier){
		Row(
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.featuredBrands),
				style = MaterialTheme.typography.titleMedium
			)
			TextButton(
				onClick = { onClickSeeAll() },
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				Text(
					text = stringResource(R.string.seeAll),
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
		Spacer(Modifier.height(16.dp))
		LazyRow{
			item{
				Spacer(Modifier.width(16.dp))
			}
			brands.forEach{ brand ->
				item{
					BrandItem(
						modifier = Modifier.width(80.dp),
						onClick = { onClickItem(it) },
						brand = brand
					)
					Spacer(Modifier.width(8.dp))
				}
			}

			item{
				Spacer(Modifier.width(8.dp))
			}
		}
	}
}

@Composable
fun HomeRecentViewed(
	modifier: Modifier = Modifier,
	onClick: (Long) -> Unit,
	onClickSeeAll: () -> Unit,
	products: List<UiProduct>,
	onToggleWishlist: (Long) -> Unit,
	onToggleCart: (Long) -> Unit,
	wishlistItems: Set<Long>,
	cartItems:  Set<Long>
){
	Column(modifier) {
		Row(
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.recentViewed),
				style = MaterialTheme.typography.titleMedium
			)
			TextButton(
				onClick = { onClickSeeAll() },
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				Text(
					text = stringResource(R.string.seeAll),
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
		Spacer(Modifier.height(16.dp))
		LazyRow {
			item { Spacer(Modifier.width(16.dp)) }
			products.forEach { product ->
				item {
					GridProductItem(
						modifier = Modifier.width(170.dp),
						onClick = { onClick(it) },
						product = product,
						onToggleCart = { onToggleCart(it) },
						onToggleWishlist = { onToggleWishlist(it) },
						wishlistItems = wishlistItems,
						cartItems = cartItems
					)
					Spacer(Modifier.width(8.dp))
				}
			}
			item { Spacer(Modifier.width(8.dp)) }
		}
	}
}

@Composable
private fun HomeNewArrival(
	modifier: Modifier = Modifier,
	onClickSeeAll: () -> Unit,
	onClickItem: (Long) -> Unit,
	products: List<UiProduct>,
	onToggleWishlist: (Long) -> Unit,
	onToggleCart: (Long) -> Unit,
	wishlistItems: Set<Long>,
	cartItems:  Set<Long>
){
	Column (modifier){
		Row(
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.newArrivals),
				style = MaterialTheme.typography.titleMedium
			)
			TextButton(
				onClick = { onClickSeeAll() },
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				Text(
					text = stringResource(R.string.seeAll),
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
		Spacer(Modifier.height(16.dp))
		ColumnGrid(
			modifier = Modifier.padding(horizontal = 16.dp),
			columns = 2,
			itemCount = products.size,
			gridWidthSpacing = 8.dp,
			gridHeightSpacing = 8.dp
		){ index ->
			GridProductItem(
				onClick = { onClickItem(it) },
				product = products[index],
				onToggleWishlist = { onToggleWishlist(it) },
				onToggleCart = { onToggleCart(it) },
				wishlistItems = wishlistItems,
				cartItems = cartItems
			)
		}
	}
}

@Composable
private fun BannerItem(
	slider: UiSlider,
	onClick: (UiSlider) -> Unit,
	modifier: Modifier = Modifier
){
	Box(
		modifier = modifier
			.height(180.dp)
			.fillMaxWidth()
			.shadow(1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLow),
		contentAlignment = Alignment.BottomStart
	){
		Image(
			painter = rememberAsyncImagePainter(model = slider.image),
			contentDescription = null,
			contentScale = ContentScale.Inside,
			modifier = Modifier.align(Alignment.CenterEnd)
		)
		Column (Modifier.padding(16.dp)){
			Text(
				text = slider.subTitle,
				style = MaterialTheme.typography.bodySmall.copy(
					color = MaterialTheme.colorScheme.onBackground,
				),
				maxLines = 1
			)
			Text(
				text = slider.title,
				style = MaterialTheme.typography.bodyLarge.copy(
					color = MaterialTheme.colorScheme.onBackground,
					fontWeight = FontWeight.W700,
				),
				maxLines = 1
			)
			Text(
				text = slider.highlightText,
				style = MaterialTheme.typography.titleLarge.copy(
					color = MaterialTheme.colorScheme.primary,
					fontWeight = FontWeight.W900
				),
				maxLines = 1
			)
			Text(
				text = slider.description,
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
				),
				maxLines = 2
			)
			Spacer(Modifier.height(4.dp))
			Button(
				onClick = { onClick(slider) },
				modifier = Modifier,
				shape = RoundedCornerShape(16.dp),
			) {
				Text(
					text = slider.buttonText,
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
	}
}

@Composable
private fun DotIndicators(
	pagerState: PagerState,
	modifier: Modifier
){
	Row (modifier = modifier.padding(bottom = 24.dp)){
		repeat(pagerState.pageCount){ index ->
			val color = if(pagerState.currentPage == index) MaterialTheme.colorScheme.primary else Color.White
			val width = if(pagerState.currentPage == index) 24.dp else 8.dp
			Box(
				modifier = Modifier
					.width(width)
					.size(8.dp)
					.clip(CircleShape)
					.background(color)
			)
			Spacer(modifier = Modifier.width(4.dp))
		}
	}
}

@Composable
private fun HomeTopAppBarAction(
	cartBadgeCountState: Int,
	onClickSearch: () -> Unit,
	onClickCart: () -> Unit,
){
	IconButton(
		onClick = { onClickSearch() }
	) {
		Icon(
			painter = painterResource(id = R.drawable.ic_search),
			contentDescription ="",
		)
	}
	IconButton(
		onClick = { onClickCart() }
	) {
		IconWithBadge(
			badge = cartBadgeCountState,
			icon = R.drawable.ic_cart_outline ,
			modifier = Modifier.padding(4.dp) ,
		)
	}
}



