package com.example.deniseshop.feature.checkout

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Checkout
import com.example.deniseshop.core.domain.model.PaymentMethod
import com.example.deniseshop.core.presentation.components.ButtonWithProgressIndicator
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.LoadingUi
import com.example.deniseshop.feature.checkout.components.AddressSection
import com.example.deniseshop.feature.checkout.components.CheckoutItem
import com.example.deniseshop.feature.checkout.components.CheckoutSuccessDialog
import com.example.deniseshop.feature.checkout.components.CheckoutSummarySection
import com.example.deniseshop.feature.checkout.components.PaymentMethodSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
	viewModel: CheckoutViewModel,
	onBackClick: () -> Unit,
	onCheckoutDone: () -> Unit,
	onEditAddAddressClick: (Long) -> Unit,
	onShowSnackBar: suspend (String, String?) -> Boolean,
) {
	val state by viewModel.state.collectAsState()

	val context = LocalContext.current

	var error by remember { mutableStateOf<String?>(null) }

	var showCheckoutSuccessDialog by remember { mutableStateOf(false) }


	state.checkoutSuccess?.let {
		showCheckoutSuccessDialog = true
	}

	error = if (state.checkoutError != null) {
		state.checkoutError!!.asString()
	} else null

	LaunchedEffect(error) {
		error?.let {
			onShowSnackBar(it,null)
			viewModel.clearState()
		}
	}

	LaunchedEffect(state.paypalPaymentUrl) {
		if (state.paypalPaymentUrl != null){
			val customTabsIntent = CustomTabsIntent.Builder()
				.setShowTitle(true)
				.build()
			customTabsIntent.launchUrl(context, state.paypalPaymentUrl!!.toUri())
			viewModel.clearState()
		}
	}

	if (showCheckoutSuccessDialog){
		state.checkoutSuccess?.let {
			CheckoutSuccessDialog(
				message = it,
				onDismiss = {
					viewModel.clearState()//clear checkoutSuccess
					showCheckoutSuccessDialog = false
				},
				onConfirm = {
					onCheckoutDone()
				}
			)
		}
	}

	Column(
		modifier = Modifier
			.windowInsetsPadding(WindowInsets.navigationBars)
			.fillMaxSize()
	) {
		TopAppBar(
			title = { Text(text = stringResource(R.string.checkout)) },
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
		)
		Box(
			modifier = Modifier
				.fillMaxSize(),
			contentAlignment = Alignment.Center
		){
			if (
				state.checkout == null &&
				state.isLoading
			){
				LoadingUi()
			}

			if (
				state.checkout == null &&
				!state.isLoading &&
				state.loadingError != null
			){
				ErrorUi(
					message = state.loadingError!!.asString(),
					onErrorAction = {
						viewModel.retry()
					}
				)
			}

			state.checkout?.let { checkout ->
				CheckoutScreen(
					checkout = checkout,
					selectedPaymentMethod = state.paymentMethod,
					isCheckingOut = state.isCheckingOut,
					onSelectPaymentOption = {
						viewModel.onSelectPaymentMethod(it)
					},
					onPlaceOrder = {
						if (state.paymentMethod != null) {
							when(state.paymentMethod!!.slug){
								"cash-on-delivery" ->{
									viewModel.placeOrder()
								}
								"paypal" ->{
									viewModel.getPaypalPaymentUrl()
								}
							}
						}
					},
					onAddClick = {
						onEditAddAddressClick(0L)
					},
					onkEditClick = onEditAddAddressClick
				)
			}
		}
	}
}


@Composable
private fun CheckoutScreen(
	checkout: Checkout,
	selectedPaymentMethod: PaymentMethod?,
	isCheckingOut: Boolean,
	onPlaceOrder: () -> Unit,
	onSelectPaymentOption: (PaymentMethod) -> Unit,
	onkEditClick: (Long) -> Unit,
	onAddClick: () -> Unit,
	modifier: Modifier = Modifier
){
	Column(
		modifier = modifier
			.fillMaxSize()
	)
	{
		LazyColumn(
			modifier = Modifier
				.weight(1f),
			contentPadding = PaddingValues(16.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			if(checkout.containPhysicalItem) {
				item {
					AddressSection(
						address = checkout.address,
						onAddClick = onAddClick,
						onEditClick = onkEditClick
					)
				}
			}

			item {
				PaymentMethodSection(
					paymentMethods = checkout.payment,
					selectedPaymentMethod = selectedPaymentMethod,
					onSelectPayment = onSelectPaymentOption
				)
			}

			items(checkout.items.size) { index ->
				CheckoutItem(
					cartProduct = checkout.items[index]
				)
			}

			item {
				CheckoutSummarySection(
					checkout = checkout
				)
			}
		}
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp, vertical = 8.dp)
		) {
			ButtonWithProgressIndicator(
				onClick = onPlaceOrder,
				isLoading = isCheckingOut,
				modifier = Modifier.fillMaxWidth(),
				shape = RoundedCornerShape(16.dp),
				progressIndicatorModifier = Modifier.scale(0.8f),
			) {
				Text(text = stringResource(R.string.placeorder))
			}
		}
	}
}