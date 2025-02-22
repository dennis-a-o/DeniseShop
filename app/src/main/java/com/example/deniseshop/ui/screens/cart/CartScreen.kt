package com.example.deniseshop.ui.screens.cart


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.components.IconWithBadge
import com.example.deniseshop.R
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.models.UiCart
import com.example.deniseshop.ui.models.UiCartProduct

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
	onNavigateUp: () ->  Unit,
	onNavigate: (String, NavOptions?) -> Unit,
	viewModel: CartViewModel,
	wishlistCount: Int = 0
){
	val isLoggedIn = viewModel.isLoggedIn.collectAsState()
	val cartState = viewModel.cartState.collectAsState()
	val responseState = viewModel.actionState.collectAsState()
	val context = LocalContext.current

	LaunchedEffect(
		key1 = responseState.value,
		key2 = isLoggedIn.value
	) {
		if (responseState.value.isError || responseState.value.isSuccess) {
			Toast.makeText(context, responseState.value.message, Toast.LENGTH_LONG).show()
			viewModel.resetActionState()
		}
		if (!isLoggedIn.value){
			onNavigate(
				Routes.SignInScreen.route,
				NavOptions.Builder().apply {
					setPopUpTo(Routes.Home.route, inclusive = false, saveState = true)
					setLaunchSingleTop(true)
					setRestoreState(true)
				}.build()
			)
		}
	}

	Scaffold(
		modifier = Modifier,
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.cart)) },
				modifier = Modifier.shadow(elevation = 1.dp),
				navigationIcon = {
					IconButton(onClick = { onNavigateUp() }) {
						Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "" )
					}
				},
				actions = {
					TopAppBarAction(
						wishlistCount = wishlistCount,
						onClickSearch = {
							onNavigate(Routes.Search.route, null)
						},
						onClickWishList = {
							onNavigate(Routes.Wishlist.route, null)
						}
					)
				}
			)
		},
		bottomBar = {
			if(cartState.value is ScreenState.Success &&  (cartState.value as ScreenState.Success<UiCart>).uiData.cartItems.isNotEmpty()) {
				CartBottomBar(
					onClearAll = { viewModel.onClearCart() },
					onCheckout = { onNavigate(Routes.Checkout.route, null) }
				)
			}
		}
	){ paddingValues ->
		Box (
			Modifier
				.padding(paddingValues)
				.fillMaxSize()
		){
			when(cartState.value){
				is ScreenState.Error -> {
					ErrorUi(onErrorAction = { viewModel.onRetry() })
				}
				is ScreenState.Loading -> {
					LoadingUi()
				}
				is ScreenState.Success -> {
					if((cartState.value as ScreenState.Success<UiCart>).uiData.cartItems.isNotEmpty()) {
						CartScreen(
							onApplyCoupon = { viewModel.onApplyCoupon(it) },
							onClearCoupon = { viewModel.onClearCoupon() },
							onRemoveCart = { viewModel.onRemoveCart(it) },
							onIncreaseQuantity = { viewModel.onIncreaseQuantity(it) },
							onDecreaseQuantity = { viewModel.onDecreaseQuantity(it) },
							onClick = {
								onNavigate("${Routes.ProductDetail.route}/$it", null)
							},
							cart = (cartState.value as ScreenState.Success<UiCart>).uiData
						)
					}
				}
			}
		}
	}
}

@Composable
private fun CartScreen(
	onApplyCoupon:(String) -> Unit,
	onClearCoupon: () -> Unit,
	onRemoveCart: (Long) -> Unit,
	onIncreaseQuantity: (Long) -> Unit,
	onDecreaseQuantity: (Long) -> Unit,
	onClick: (Long) -> Unit,
	cart: UiCart
){
	LazyColumn(
		modifier = Modifier
			.padding(horizontal = 8.dp)
			.padding(top = 8.dp)
	) {
		cart.cartItems.forEach { cartItem ->
			item {
				CartItem(
					onClear = { onRemoveCart(it) },
					onQuantityInc = { onIncreaseQuantity(it) },
					onQuantityDec = { onDecreaseQuantity(it)  },
					onClick = { onClick(it) },
					cartItem = cartItem
				)
				Spacer(Modifier.height(8.dp))
			}
		}
		item {
			CouponForm(
				onSubmitCoupon = { onApplyCoupon(it) }
			)
		}
		item {
			Spacer(Modifier.height(8.dp))
			CartSummary(
				onClearCoupon = { onClearCoupon() },
				cart = cart
			)
			Spacer(Modifier.height(16.dp))
		}
	}
}

@Composable
private fun CartItem(
	modifier: Modifier = Modifier,
	onClear: (Long) -> Unit,
	onQuantityInc: (Long) -> Unit,
	onQuantityDec: (Long) -> Unit,
	onClick: (Long) -> Unit,
	cartItem: UiCartProduct
) {
	Row(
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable { onClick(cartItem.productId) }
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceEvenly,
		verticalAlignment = Alignment.Top
	) {
		Image(
			painter = rememberAsyncImagePainter(model = cartItem.image),
			modifier = Modifier
				.width(100.dp)
				.height(120.dp)
				.clip(RoundedCornerShape(16.dp)),
			contentDescription = "",
			contentScale = ContentScale.Crop
		)
		Column(modifier = Modifier.padding(8.dp)) {
			Text(
				text = cartItem.name,
				style = MaterialTheme.typography.bodyLarge,
				maxLines = 2,
			)
			Row (verticalAlignment = Alignment.CenterVertically){
				Text(
					text = cartItem.activePrice,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground,
						fontWeight = FontWeight.W500
					)
				)
				Spacer(Modifier.width(4.dp))
				if(cartItem.percentageDiscount < 0) {
					Text(
						text = cartItem.price,
						style = MaterialTheme.typography.bodySmall.copy(
							color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
							textDecoration = TextDecoration.LineThrough
						)
					)
				}
			}
			cartItem.color?.let {
				Row {
					Text(
						text = "Color:",
						style = MaterialTheme.typography.bodyMedium.copy(
							color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
						)
					)
					Spacer(Modifier.width(4.dp))
					Text(
						text = cartItem.color,
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
			cartItem.size?.let {
				Row {
					Text(
						text = "Size:",
						style = MaterialTheme.typography.bodyMedium.copy(
							color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
						)
					)
					Spacer(Modifier.width(4.dp))
					Text(
						text = cartItem.size,
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
			Spacer(Modifier.height(4.dp))
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.Top
			) {
				Row(
					horizontalArrangement = Arrangement.Start,
					verticalAlignment = Alignment.CenterVertically
				) {
					IconButton(
						onClick = { onQuantityDec(cartItem.id) },
						modifier = Modifier
							.shadow(1.dp, CircleShape)
							.size(32.dp),
						colors = IconButtonDefaults
							.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
					) {
						Icon(
							painter = painterResource(R.drawable.ic_remove),
							contentDescription = "",
							modifier = Modifier.size(14.dp),
							tint = MaterialTheme.colorScheme.primary
						)
					}
					Spacer(Modifier.width(8.dp))
					Text(
						text = cartItem.quantity.toString(),
						style = MaterialTheme.typography.bodyMedium
					)
					Spacer(Modifier.width(8.dp))
					IconButton(
						onClick = { onQuantityInc(cartItem.id) },
						modifier = Modifier
							.shadow(1.dp, CircleShape)
							.size(32.dp),
						colors = IconButtonDefaults
							.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
					) {
						Icon(
							painter = painterResource(R.drawable.ic_add),
							contentDescription = "",
							modifier = Modifier.size(14.dp),
							tint = MaterialTheme.colorScheme.primary
						)
					}
				}
				IconButton(
					onClick = {  onClear(cartItem.productId) },
					modifier = Modifier.size(32.dp)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_close),
						contentDescription = "",
						modifier = Modifier.scale(0.8f)
					)
				}
			}
		}
	}
}

@Composable
private fun CouponForm(
	onSubmitCoupon: (String) -> Unit,
){
	var couponValue by remember { mutableStateOf("") }

	Row (
		modifier = Modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		TextField(
			value = couponValue,
			onValueChange = { couponValue = it },
			modifier = Modifier.fillMaxWidth(0.7f),
			placeholder = {
				Text(
					text = stringResource(R.string.haveACouponCode),
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
					)
				)
			},
			singleLine = true,
			colors = TextFieldDefaults.colors(
				unfocusedIndicatorColor = Color.Transparent,
				focusedIndicatorColor = Color.Transparent,
				unfocusedContainerColor = Color.Transparent,
				focusedContainerColor = Color.Transparent,
			)
		)
		Button(
			onClick = {
				if (couponValue.isNotEmpty()) {
					onSubmitCoupon(couponValue)
					couponValue = ""
				}
			},
			shape = RoundedCornerShape(16.dp)
		) {
			Text(text = stringResource(id = R.string.apply))
		}
	}
}

@Composable
private fun CartSummary(
	modifier: Modifier = Modifier,
	onClearCoupon: () -> Unit,
	cart: UiCart
){
	Column(
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
			.fillMaxWidth()
	){
		cart.coupon?.let {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Row (
					modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
				){
					Text(
						text = "Coupon Discount",
						style = MaterialTheme.typography.bodyMedium.copy(
							color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
						)
					)
					Text(
						text = stringResource(R.string.Delete),
						modifier = Modifier.padding(start = 4.dp).clickable { onClearCoupon() },
						style = MaterialTheme.typography.bodySmall.copy(
							color = MaterialTheme.colorScheme.error
						)
					)
				}

				Text(
					text = if(cart.couponType == "free_shipping") "Free shipping" else cart.couponDiscount,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
					)
				)
			}
		}
		Row (
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		){
			Text(
				text = stringResource(R.string.totalPrice),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
			Text(
				text = cart.totalPrice,
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground,
					fontWeight = FontWeight.W500
				)
			)
		}
	}
}

@Composable
private fun TopAppBarAction(
	wishlistCount: Int,
	onClickSearch: () -> Unit,
	onClickWishList: () -> Unit,
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
		onClick = { onClickWishList() }
	) {
		IconWithBadge(
			badge = wishlistCount,
			icon = R.drawable.ic_favorite_outline ,
			modifier = Modifier.padding(4.dp) ,
		)
	}
}

@Composable
fun CartBottomBar(
	modifier: Modifier = Modifier,
	onClearAll: () -> Unit,
	onCheckout: () -> Unit
){
	var dialogState by remember { mutableStateOf(false) }

	if (dialogState){
		AlertDialog(
			onDismissRequest = { dialogState = false },
			confirmButton = {
				OutlinedButton(onClick = { dialogState = false }) {
					Text(text = stringResource(R.string.cancel))
				}
				Button(onClick = { dialogState = false; onClearAll() }) {
					Text(text = stringResource(R.string.ok))
				}
			},
			title = { Text(text = stringResource(R.string.confirm) ) },
			text = { Text(text = stringResource(R.string.confirmClearCartText)) }
		)
	}
	BottomAppBar (
		modifier = Modifier.shadow(elevation = 8.dp),
		containerColor = MaterialTheme.colorScheme.surface,
	){
		Row(
			modifier = modifier
				.fillMaxSize()
				.padding(8.dp),
			horizontalArrangement = Arrangement.SpaceEvenly,
			verticalAlignment = Alignment.CenterVertically
		) {
			OutlinedButton(
				onClick = { dialogState = true },
				modifier = Modifier.fillMaxWidth(0.5f),
				shape = RoundedCornerShape(16.dp)
			) {
				Text(text = stringResource(R.string.clearALl))
			}
			Spacer(Modifier.width(8.dp))
			Button(
				onClick = { onCheckout() },
				modifier = Modifier.fillMaxWidth(),
				shape = RoundedCornerShape(16.dp)
			) {
				Text(text = stringResource(R.string.checkout))
			}
		}
	}
}

