package com.example.deniseshop.ui.screens.product

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.R
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.components.IconWithBadge
import com.example.deniseshop.ui.components.RatingStar
import com.example.deniseshop.ui.components.ReviewItem
import com.example.deniseshop.ui.components.ReviewStatItem
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.FullScreenDialog
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.models.UiProduct
import com.example.deniseshop.ui.models.UiProductDetail
import com.example.deniseshop.ui.screens.cart.CartViewModel
import com.example.deniseshop.ui.screens.product.viewModels.ProductDetailEvent
import com.example.deniseshop.ui.screens.product.viewModels.ProductDetailViewModel
import com.example.deniseshop.ui.screens.wishlist.WishlistViewModel
import com.example.deniseshop.ui.theme.Orange100
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
	onNavigate: (String, NavOptions?) -> Unit,
	onNavigateUp: () -> Unit,
	viewModel: ProductDetailViewModel,
	wishlistViewModel: WishlistViewModel,
	cartViewModel: CartViewModel
){
	val isProductDescriptionVisible = viewModel.isProductDescriptionVisible.collectAsState()
	val productDetailState = viewModel.productDetailState.collectAsState()
	val quantity = viewModel.quantity.collectAsState()
	val color = viewModel.color.collectAsState()
	val size = viewModel.size.collectAsState()

	val productId = if(productDetailState.value is ScreenState.Success){
		(productDetailState.value as ScreenState.Success<UiProductDetail>).uiData.product.id
	} else 0

	val context = LocalContext.current

	val wishlistCount = wishlistViewModel.wishlistCount.collectAsState()
	val wishlistItems = wishlistViewModel.wishlistItems.collectAsState()
	val wishlistState = wishlistViewModel.responseState.collectAsState()

	val cartCount = cartViewModel.cartCountState.collectAsState()
	val cartItems = cartViewModel.cartItems.collectAsState()
	val cartState = cartViewModel.actionState.collectAsState()

	LaunchedEffect(wishlistState.value) {
		if (wishlistState.value.isError || wishlistState.value.isSuccess) {
			Toast.makeText(context, wishlistState.value.message, Toast.LENGTH_LONG).show()
			wishlistViewModel.resetResponseState()
		}
	}

	LaunchedEffect(cartState.value) {
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
						text = if(productDetailState.value is ScreenState.Success){
							(productDetailState.value as ScreenState.Success<UiProductDetail>).uiData.product.name
						}else "" ,
						maxLines = 1,
						overflow = TextOverflow.Ellipsis
					)
				},
				modifier = Modifier.shadow(elevation = 1.dp),
				 navigationIcon = {
					IconButton(onClick = { onNavigateUp() }) {
						Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "" )
					}
				},
				actions = {
					TopAppBarAction(
						onClickSearch = {onNavigate(Routes.Search.route,null) },
						onClickWishlist = { onNavigate(Routes.Wishlist.route,null) },
						onClickCart = { onNavigate(Routes.Cart.route,null) },
						cartCount = cartCount.value,
						wishlistCount = wishlistCount.value
					)
				}
			)
		},
		bottomBar = {
			if(productDetailState.value is ScreenState.Success<UiProductDetail>) {
				ProductDetailFooter(
					onAddToCart = {
						cartViewModel.onAddCart(productId, quantity.value, color.value, size.value)
					},
					onClickHome = {
						onNavigate(
							Routes.Home.route,
							NavOptions.Builder().apply {
								setPopUpTo(Routes.Home.route, inclusive = false)
								setLaunchSingleTop(true)
							}.build()
						)
					},
					onClickViewCart = { onNavigate(Routes.Cart.route,null) },
					isInCart = cartItems.value.contains(productId)
				)
			}
		}
	){ paddingValues ->
		Box(
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize()
		){
			when(productDetailState.value){
				is ScreenState.Loading -> {
					LoadingUi()
				}

				is ScreenState.Error -> {
					ErrorUi(onErrorAction = { viewModel.onRetry() })
				}

				is ScreenState.Success -> {
					ProductDetailScreen(
						productDetailData = (productDetailState.value as ScreenState.Success<UiProductDetail>).uiData,
						onEvent = { viewModel.onEvent(it) },
						onSeeAllReview = {
							onNavigate("${Routes.Reviews.route}/$it",null)
						},
						onSeeAllDescription = {
							viewModel.onEvent(ProductDetailEvent.ProductDescriptionVisible(true))
						},
						onToggleWishlist = { wishlistViewModel.onToggleWishlist(it) },
						onClickBrand = {
							onNavigate("${Routes.BrandProductScreen.route}/$it/0",null)
						},
						onClickCategory = {
							onNavigate("${Routes.CategoryProductScreen.route}/$it",null)
						},
						selectedSize = size.value,
						selectedColor = color.value,
						selectedQuantity = quantity.value,
						inWishlist = wishlistItems.value.contains(productId),
						inCart = cartItems.value.contains(productId)
					)
				}
			}
		}

		if (isProductDescriptionVisible.value){
			FullScreenDialog(onDismiss = { viewModel.onEvent(ProductDetailEvent.ProductDescriptionVisible(false)) }) {
				ProductFullDescription(
					onClose = { viewModel.onEvent(ProductDetailEvent.ProductDescriptionVisible(false)) },
					description = if(productDetailState.value is ScreenState.Success<UiProductDetail>){
						(productDetailState.value as ScreenState.Success<UiProductDetail>).uiData.product.description?:"Oops"
					}else{ "Oops no description." }
				)
			}
		}
	}
}

@Composable
private fun ProductDetailScreen(
	productDetailData: UiProductDetail,
	onEvent: (ProductDetailEvent) -> Unit,
	onSeeAllReview: (Long) -> Unit,
	onToggleWishlist: (Long) -> Unit,
	onSeeAllDescription: () -> Unit,
	onClickBrand: (Long) -> Unit,
	onClickCategory: (Long) -> Unit,
	selectedSize: String,
	selectedColor: String,
	selectedQuantity: Int,
	inWishlist: Boolean,
	inCart: Boolean
){
	LazyColumn {
		item {
			GallerySection(
				images = productDetailData.product.gallery.orEmpty().plus(productDetailData.product.image)
			)
		}
		item {
			ProductInfo(
				product = productDetailData.product,
				onToggleWishlist = { onToggleWishlist(it) },
				onEvent = { onEvent(it) },
				onClickCategory = {
					onClickCategory(it)
				},
				onClickBrand = {
					onClickBrand(it)
				},
				selectedQuantity = selectedQuantity,
				inWishlist = inWishlist,
				inCart = inCart
			)
			Spacer(Modifier.height(8.dp))
		}
		if (!(productDetailData.product.color.isNullOrEmpty()) || !(productDetailData.product.size.isNullOrEmpty())) {
			item {
				ProductAttribute(
					product = productDetailData.product,
					selectedSize = selectedSize,
					selectedColor = selectedColor,
					onSelectSize = { onEvent(it) },
					onSelectColor = { onEvent(it) }
				)
				Spacer(Modifier.height(8.dp))
			}
		}
		item {
			ProductDescription(
				onSeeAllSpecification = { onSeeAllDescription() },
				description = productDetailData.product.descriptionSummary
			)
			Spacer(Modifier.height(8.dp))
		}
		item {
			CustomerReview(
				onSeeAllReview = { onSeeAllReview(it)  },
				productDetailData = productDetailData
			)
			Spacer(Modifier.height(8.dp))
		}
	}
}

@Composable
private fun GallerySection(
	images: List<String>,
	modifier: Modifier = Modifier,
	pagerState: PagerState = rememberPagerState(
		initialPage = 0,
		initialPageOffsetFraction = 0f,
		pageCount = { images.size }
	)
){
	Box (
		modifier = modifier.fillMaxWidth(),
	){
		HorizontalPager(
			state = pagerState,
			pageSize = PageSize.Fill,
			contentPadding = PaddingValues(8.dp),
			pageSpacing = 8.dp
		) { index ->
			Image(
				painter = rememberAsyncImagePainter(model = images[index]),
				contentDescription = "",
				modifier = Modifier
					.height(200.dp)
					.fillMaxWidth()
					.clip(RoundedCornerShape(16.dp))
					.background(color = MaterialTheme.colorScheme.surfaceContainer),
				contentScale = ContentScale.Crop
			)
		}
		Row (
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.fillMaxWidth()
				.offset(y = (-16).dp),
			horizontalArrangement = Arrangement.Center
		){
			repeat(pagerState.pageCount){ index ->
				val color = if(pagerState.currentPage == index) MaterialTheme.colorScheme.primary else Color.White
				val width = if(pagerState.currentPage == index) 24.dp else 8.dp

				Box(
					modifier = Modifier
						.width(width)
						.size(8.dp)
						.clip(CircleShape)
						.background(color)
						.border(
							width = 1.dp,
							color = MaterialTheme.colorScheme.primary,
							shape = CircleShape
						)
				)
				Spacer(modifier = Modifier.width(4.dp))
			}
		}
	}
}

@Composable
private fun ProductInfo(
	product: UiProduct ,
	onToggleWishlist: (Long) -> Unit,
	onEvent: (ProductDetailEvent) -> Unit,
	onClickBrand: (Long) -> Unit,
	onClickCategory: (Long) -> Unit,
	selectedQuantity: Int,
	inWishlist: Boolean,
	inCart: Boolean
){
	Column(modifier = Modifier
		.padding(horizontal = 8.dp)
		.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
		.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
		.padding(8.dp)
		.fillMaxWidth()
	) {
		Text(
			text =product.name,
			style = MaterialTheme.typography.titleMedium,
			maxLines = 3
		)
		Spacer(Modifier.height(8.dp))
		Row (
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		){
			Row(verticalAlignment = Alignment.CenterVertically) {
				Text(
					text = product.activePrice,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					),
					maxLines = 1,
				)
				Spacer(Modifier.width(8.dp))
				if (product.percentageDiscount < 0) {
					Text(
						text = product.price,
						style = MaterialTheme.typography.bodySmall.copy(
							color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
							textDecoration = TextDecoration.LineThrough
						)
					)
					Spacer(Modifier.width(8.dp))
					Text(
						text = "${String.format(Locale.getDefault(),"%.0f", product.percentageDiscount)}%",
						modifier = Modifier
							.background(
								MaterialTheme.colorScheme.inversePrimary,
								RoundedCornerShape(8.dp)
							)
							.padding(horizontal = 8.dp),
						color = MaterialTheme.colorScheme.primary,
						style = MaterialTheme.typography.bodySmall,
						maxLines = 1
					)
				}
			}
			if(!inCart) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					IconButton(
						onClick = { onEvent(ProductDetailEvent.MinusQuantity(selectedQuantity)) },
						modifier = Modifier
							.shadow(2.dp, CircleShape)
							.size(32.dp),
						colors = IconButtonDefaults
							.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_remove),
							contentDescription = null,
							modifier = Modifier.size(12.dp),
							tint = MaterialTheme.colorScheme.primary
						)
					}
					Spacer(Modifier.width(8.dp))
					Text(
						text = selectedQuantity.toString(),
						style = MaterialTheme.typography.bodySmall
					)
					Spacer(Modifier.width(8.dp))
					IconButton(
						onClick = { onEvent(ProductDetailEvent.AddQuantity(selectedQuantity)) },
						modifier = Modifier
							.shadow(2.dp, CircleShape)
							.size(32.dp),
						colors = IconButtonDefaults
							.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_add),
							contentDescription = null,
							modifier = Modifier.size(12.dp),
							tint = MaterialTheme.colorScheme.primary
						)
					}
				}
			}
		}
		Row {
			if (product.quantity == 0){
				Text(
					text = stringResource(R.string.outOfStock),
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.error
					)
				)
			}else if( product.quantity < 5){
				Text(
					text = stringResource(R.string.fewUnitLeft),
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
					)
				)
			}else{
				Text(
					text = stringResource(R.string.inStock),
					style = MaterialTheme.typography.bodyMedium.copy(
						color = Color.Green
					)
				)
			}
		}
		Spacer(Modifier.height(8.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		){
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				RatingStar(
					rating = product.averageRating.toInt(),
					onStartClick = {},
					starModifier = Modifier.size(14.dp),
					startTint = Orange100
				)
				Spacer(Modifier.width(8.dp))
				Text(
					text = "(${product.reviewCount} reviews)",
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
					),
				)
			}
			IconButton(
				onClick = { onToggleWishlist(product.id) },
				modifier = Modifier.size(20.dp)
			) {
				Icon(
					painter = painterResource(id = if(inWishlist){
						R.drawable.ic_favorite_filled
					}else{
						R.drawable.ic_favorite_outline
					}) ,
					contentDescription = "" ,
					tint = MaterialTheme.colorScheme.primary
				)
			}
		}
		product.brand?.let { brand ->
			Spacer(Modifier.height(8.dp))
			Row {
				Text(
					text = "Brand:",
					style = MaterialTheme.typography.bodyMedium
				)
				Spacer(Modifier.width(8.dp))
				Text(
					text = brand.name,
					modifier = Modifier.clickable {
						onClickBrand(brand.id)
					},
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
					)
				)
			}
		}
		product.categories?.let { categories ->
			Spacer(Modifier.height(8.dp))
			Row {
				Text(
					text = "Category:",
					style = MaterialTheme.typography.bodyMedium
				)
				Spacer(Modifier.width(8.dp))
				categories.forEach {
					Text(
						text = it.name,
						modifier = Modifier.clickable {
							onClickCategory(it.id)
						},
						style = MaterialTheme.typography.bodyMedium.copy(
							color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
						)
					)
					Spacer(Modifier.width(4.dp))
				}
			}
		}
	}
}

@Composable
private fun ProductAttribute(
	product: UiProduct,
	onSelectColor: (ProductDetailEvent) -> Unit,
	onSelectSize: (ProductDetailEvent) -> Unit,
	selectedColor: String,
	selectedSize: String,
){
	Column(
		modifier = Modifier
			.padding(horizontal = 8.dp)
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.fillMaxWidth()
			.padding(8.dp)
	) {
		product.color?.let {
			Row {
				Text(
					text = "Color:",
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					)
				)
				Spacer(Modifier.width(8.dp))
				Text(
					text = selectedColor,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
					)
				)
			}
			Spacer(Modifier.height(8.dp))
			LazyRow {
				product.color.forEach {
					val color = it.split("=")
					item {
						IconButton(
							onClick = {  onSelectColor(ProductDetailEvent.SelectColor(color[0])) },
							modifier = Modifier
								.shadow(1.dp, CircleShape)
								.size(28.dp),
							colors = IconButtonDefaults
								.iconButtonColors(containerColor = Color(android.graphics.Color.parseColor(color[1])))
						) {
							if (selectedColor == color[0])
								Icon(
									painter = painterResource(id = R.drawable.ic_done),
									contentDescription = "",
									modifier = Modifier.scale(0.8f),
									tint = MaterialTheme.colorScheme.surfaceContainerLow
								)
						}
						Spacer(Modifier.width(8.dp))
					}
				}
			}
		}
		product.size?.let {
			Spacer(Modifier.height(16.dp))
			Row {
				Text(
					text = "Size:",
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					)
				)
				Spacer(Modifier.width(8.dp))
				Text(
					text = selectedSize,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
					)
				)
			}
			Spacer(Modifier.height(8.dp))
			LazyRow {
				product.size.forEach { size ->
					item {
						ElevatedFilterChip(
							selected = selectedSize == size,
							onClick = { onSelectSize(ProductDetailEvent.SelectSize(size)) },
							label = { Text(text = size ) },
							modifier = Modifier.padding(end = 8.dp),
							colors = FilterChipDefaults
								.filterChipColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
						)
					}
				}
			}
		}
	}
}

@Composable 
private fun ProductDescription(
	onSeeAllSpecification: () -> Unit,
	description: String?,
){
	Column(
		modifier = Modifier
			.padding(horizontal = 8.dp)
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.fillMaxWidth()
			.padding(8.dp)
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.descriptions),
				style = MaterialTheme.typography.titleMedium
			)
			TextButton(
				onClick = { onSeeAllSpecification() },
				modifier = Modifier.padding(0.dp),
			) {
				Text(
					text = stringResource(R.string.seeAll),
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
		description?.let {
			Text(
				text = it,
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
				)
			)
		}
	}
}

@Composable
private fun CustomerReview(
	productDetailData: UiProductDetail,
	onSeeAllReview: (Long) -> Unit,
){
	Column (
		modifier = Modifier
			.padding(horizontal = 8.dp)
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
	){
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.reviews),
				style = MaterialTheme.typography.titleMedium
			)
			TextButton(onClick = { onSeeAllReview(productDetailData.product.id) }) {
				Text(
					text = stringResource(R.string.seeAll),
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
		ReviewStatItem(reviewStat = productDetailData.reviewStat)
		productDetailData.reviews.forEach {
			Spacer(Modifier.height(16.dp))
			HorizontalDivider(thickness = 0.5.dp)
			Spacer(Modifier.height(16.dp))
			ReviewItem(
				review = it
			)
		}
	}
}

@Composable
private fun ProductDetailFooter(
	onAddToCart: () -> Unit,
	onClickHome: () -> Unit,
	onClickViewCart: () -> Unit,
	isInCart: Boolean
){
	BottomAppBar(
		modifier = Modifier.shadow(elevation = 8.dp),
		containerColor = MaterialTheme.colorScheme.background
	) {
		Row (
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceAround
		){
			OutlinedButton(
				onClick = { onClickHome() },
				shape = RoundedCornerShape(16.dp),
				modifier = Modifier.fillMaxWidth(fraction = 0.2f)
			) {
				Icon(
					painter = painterResource(R.drawable.ic_home_outline) ,
					contentDescription = "",
					modifier = Modifier.size(20.dp)
				)
			}
			Spacer(Modifier.width(8.dp))
			if(isInCart){
				Button(
					onClick = { onClickViewCart() },
					shape = RoundedCornerShape(16.dp),
					modifier = Modifier.fillMaxWidth()
				) {
					Text(text = stringResource(R.string.viewInCart))
				}
			}else {
				Button(
					onClick = { onAddToCart() },
					shape = RoundedCornerShape(16.dp),
					modifier = Modifier.fillMaxWidth()
				) {
					Row {
						Icon(
							painter = painterResource(R.drawable.ic_add_shopping_cart),
							contentDescription = "",
							modifier = Modifier.size(20.dp)
						)
						Spacer(Modifier.width(8.dp))
						Text(text = stringResource(R.string.addToCart))
					}
				}
			}
		}
	}
}

@Composable
private fun TopAppBarAction(
	onClickSearch: () -> Unit,
	onClickWishlist: () -> Unit,
	onClickCart: () -> Unit,
	cartCount: Int,
	wishlistCount: Int,
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
		onClick = { onClickWishlist() }
	) {
		IconWithBadge(
			badge = wishlistCount,
			icon = R.drawable.ic_favorite_outline ,
			modifier = Modifier.padding(4.dp) ,
		)
	}
	IconButton(
		onClick = { onClickCart() }
	) {
		IconWithBadge(
			badge = cartCount,
			icon = R.drawable.ic_cart_outline ,
			modifier = Modifier.padding(4.dp) ,
		)
	}
}
