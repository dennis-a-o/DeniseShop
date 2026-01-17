package com.example.deniseshop.feature.cart

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Cart
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.IconWithBadge
import com.example.deniseshop.core.presentation.components.LoadingUi
import com.example.deniseshop.feature.cart.components.CartBottomBar
import com.example.deniseshop.feature.cart.components.CartItem
import com.example.deniseshop.feature.cart.components.CartSummary
import com.example.deniseshop.feature.cart.components.CouponForm
import com.example.deniseshop.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
	viewModel: CartViewModel = hiltViewModel(),
	onBackClick: () -> Unit,
	onNavigate: (String) -> Unit,
	onShowSnackBar: suspend (String, String?) -> Boolean
) {
	val state by viewModel.state.collectAsState()
	val wishlistItems by viewModel.wishlistItems.collectAsState()

	var error by remember {  mutableStateOf<String?>(null) }

	val context = LocalContext.current

	if (state.success != null){
		Toast.makeText(context, state.success,Toast.LENGTH_LONG).show()
		viewModel.onEvent(CartEvent.ClearCartState)
	}

	LaunchedEffect(error) {
		error?.let {
			onShowSnackBar(it, null)
			viewModel.onEvent(CartEvent.ClearCartState)
		}
	}

	error = if (state.error != null){
		state.error!!.asString()
	} else null

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		TopAppBar(
			title = { Text(text = stringResource(R.string.cart)) },
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
				viewModel.onEvent(CartEvent.Refresh)
			},
			modifier = Modifier
				.fillMaxWidth()
				.weight(100f)
		) {
			if (state.isLoading){
				LoadingUi(
					modifier = Modifier
						.fillMaxSize()
				)
			}

			if(!state.isLoading && state.cart != null){
				if (state.cart!!.cartItems.isEmpty()){
					Text(
						text = stringResource(R.string.no_items),
						modifier = Modifier
							.align(Alignment.Center),
						style = MaterialTheme.typography.titleLarge,
						color = MaterialTheme.colorScheme.outline
					)
				}else {
					CartContent(
						cart = state.cart!!,
						isCouponLoading = state.isCouponLoading,
						onCartItemClick = { id ->
							onNavigate("${Routes.ProductDetail.route}/$id")
						},
						onEvent = { event ->
							viewModel.onEvent(event)
						}
					)
				}
			}
		}

		if (!state.isLoading  && state.cart == null){
			ErrorUi(
				onErrorAction = {
					viewModel.onEvent(CartEvent.Refresh)
				}
			)
		}

		state.cart?.let {
			if(it.cartItems.isNotEmpty()) {
				CartBottomBar(
					onClearAllClick = {
						viewModel.onEvent(CartEvent.ClearCart)
					},
					onCheckoutClick = {
						onNavigate(Routes.Checkout.route)
					}
				)
			}
		}

	}
}

@Composable
private fun CartContent(
	cart: Cart,
	isCouponLoading: Boolean,
	onCartItemClick: (Long) -> Unit,
	onEvent: (CartEvent) -> Unit,
	modifier: Modifier = Modifier
){
	LazyColumn(
		modifier = modifier,
		contentPadding = PaddingValues(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		items(cart.cartItems.size){ index ->
			CartItem(
				cartItem = cart.cartItems[index],
				onClick = onCartItemClick,
				onClearClick = {
					onEvent(CartEvent.RemoveFromCart(it))
				},
				onDecreaseClick = {
					onEvent(CartEvent.DecreaseQuantity(it))
				},
				onIncreaseClick = {
					onEvent(CartEvent.IncreaseQuantity(it))
				}
			)
		}

		item {
			CouponForm(
				isCouponLoading = isCouponLoading,
				onSubmitCoupon = {
					onEvent(CartEvent.ApplyCoupon(it))
				}
			)
		}

		item {
			CartSummary(
				cart = cart,
				onClearCoupon = {
					onEvent(CartEvent.ClearCoupon)
				}
			)
		}
	}
}