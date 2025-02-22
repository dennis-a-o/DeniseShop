package com.example.deniseshop.ui.screens.checkout

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.R
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.components.ButtonWithProgressIndicator
import com.example.deniseshop.ui.components.OrderSuccessDialog
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.models.UiAddress
import com.example.deniseshop.ui.models.UiCartProduct
import com.example.deniseshop.ui.models.UiCheckout
import com.example.deniseshop.ui.models.UiPaymentMethod
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
	onNavigate: (String,NavOptions?) -> Unit,
	onNavigateAddress: (UiAddress, NavOptions?) -> Unit,
	onNavigateUp: () -> Unit,
	viewIntentData: Uri?,
	onClearIntentData:() -> Unit,
	viewModel: CheckoutViewModel = hiltViewModel()
){
	val checkoutState = viewModel.checkoutState.collectAsState()
	val selectedPaymentMethod = viewModel.selectedPaymentMethod.collectAsState()
	val actionState  = viewModel.actionState.collectAsState()
	val paypalPaymentUrl = viewModel.paypalPaymentUrl.collectAsState()
	val orderSuccess = viewModel.orderSuccess.collectAsState()
	val orderMessage = viewModel.orderMessage.collectAsState()

	val snackBarHostState = remember { SnackbarHostState() }
	val coroutineScope = rememberCoroutineScope()
	val context = LocalContext.current

	LaunchedEffect(key1 = viewIntentData) {
		if (viewIntentData != null) {
			val status = viewIntentData.getQueryParameter("status")
			val token = viewIntentData.getQueryParameter("token")?: ""
			val payerId = viewIntentData.getQueryParameter("PayerID")?: ""

			if (status == "success") {
				viewModel.onPaypalPaymentSuccess(token,payerId)
			} else {
				viewModel.onPaypalPaymentCancel()
			}
			onClearIntentData()
		}
	}

	LaunchedEffect(paypalPaymentUrl.value) {
		if (paypalPaymentUrl.value.isNotEmpty()){
			val customTabsIntent = CustomTabsIntent.Builder()
				.setShowTitle(true)
				.build()
			customTabsIntent.launchUrl(context,Uri.parse(paypalPaymentUrl.value))
			viewModel.clearPaypalPaymentUrl()
		}
	}

	LaunchedEffect(actionState.value) {
		if (actionState.value.isError) {
			coroutineScope.launch {
				snackBarHostState.showSnackbar(
					message = actionState.value.message,
					duration = SnackbarDuration.Long,
				)
			}
			viewModel.resetActionState()
		}
		if (actionState.value.isSuccess) {
			Toast.makeText(context, actionState.value.message, Toast.LENGTH_LONG).show()
			viewModel.resetActionState()
		}
	}

	Scaffold(
		modifier = Modifier,
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.checkout)) },
				modifier = Modifier.shadow(elevation = 1.dp),
				navigationIcon = {
					IconButton(onClick = { onNavigateUp() }) {
						Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "" )
					}
				}
			)
		},
		bottomBar = {
			if (checkoutState.value is ScreenState.Success){
				CheckoutBottomBar(
					onPlaceOrder = {
						selectedPaymentMethod.value.let {
							when(selectedPaymentMethod.value?.slug){
								"cash-on-delivery" ->{
									viewModel.onPlaceOrder()
								}
								"paypal" ->{
									viewModel.onCreatePaypalPayment()
								}
							}
						}
					},
					isLoading = actionState.value.isLoading
				)
			}
		},
		snackbarHost = {
			SnackbarHost(
				hostState = snackBarHostState,
			)
		}
	){ paddingValues ->
		Box (
			Modifier
				.padding(paddingValues)
				.fillMaxSize()
		){
			when(checkoutState.value){
				is ScreenState.Error -> {
					ErrorUi(onErrorAction = { viewModel.onRetry() })
				}
				is  ScreenState.Loading -> {
					LoadingUi()
				}
				is ScreenState.Success ->{
					CheckoutScreen(
						checkoutData = (checkoutState.value as ScreenState.Success<UiCheckout>).uiData,
						selectedPaymentMethod = selectedPaymentMethod.value,
						onSelectPaymentOption = { viewModel.onSelectPaymentMethod(it) },
						onClickEdit = {
							onNavigateAddress(it, null)
						},
						onClickAdd = {
							onNavigateAddress(UiAddress(), null)
						}
					)
				}
			}
		}
		if (orderSuccess.value){
			Dialog(onDismissRequest = {}) {
				OrderSuccessDialog(
					onClickContinue = {
						onNavigate(Routes.Home.route, null)
					},
					orderMessage = orderMessage.value
				)
			}
		}
	}
}

@Composable
private fun CheckoutScreen(
	checkoutData: UiCheckout,
	selectedPaymentMethod: UiPaymentMethod?,
	onSelectPaymentOption: (UiPaymentMethod) -> Unit,
	onClickEdit: (UiAddress) -> Unit,
	onClickAdd: () -> Unit
){
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = 8.dp)
			.padding(top = 8.dp),
	) {
		if(checkoutData.containPhysicalItem) {
			item {
				AddressSection(
					address = checkoutData.address,
					onClickEdit = {
						onClickEdit(it)
					},
					onClickAdd = { onClickAdd() }
				)
				Spacer(Modifier.height(8.dp))
			}
		}
		item{
			PaymentMethodList(
				paymentMethods = checkoutData.payment,
				selectedPaymentMethod = selectedPaymentMethod,
				onSelectPayment = { onSelectPaymentOption(it) }
			)
			Spacer(Modifier.height(8.dp))
		}
		items(checkoutData.items.size){
			CheckoutItem(
				cartProduct = checkoutData.items[it]
			)
			Spacer(Modifier.height(8.dp))
		}
		item {
			Spacer(Modifier.height(8.dp))
			CheckoutSummary(
				checkoutData = checkoutData
			)
			Spacer(Modifier.height(8.dp))
		}
	}
}

@Composable
private fun AddressSection(
	modifier: Modifier = Modifier,
	address: UiAddress?,
	onClickEdit: (UiAddress) -> Unit,
	onClickAdd: () -> Unit,
){
	Column (
		modifier = modifier
			.fillMaxWidth()
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_location_on),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary,
				modifier = Modifier.scale(0.8f)
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = stringResource(R.string.address),
				style = MaterialTheme.typography.titleMedium
			)
		}
		Spacer(Modifier.height(8.dp))
		if(address != null){
			Column (Modifier.padding(8.dp)){
				 Row (
					 modifier = Modifier.fillMaxWidth(),
					 horizontalArrangement = Arrangement.SpaceBetween
				 ){
					 Text(
						 text = address.name,
						 style = MaterialTheme.typography.bodyMedium.copy(
							 fontWeight = FontWeight.W500
						 )
					 )
					 Text(
						 text = "Edit",
						 modifier = Modifier.clickable { onClickEdit(address) },
						 style = MaterialTheme.typography.bodyMedium.copy(
							 color = MaterialTheme.colorScheme.primary
						 )
					 )
				}
				Text(
					text = "${address.email}\n${address.phone}\n${address.address},${address.state},${address.city},${address.country}",
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
					)
				)
			}
		}else {
			Column(Modifier.padding(8.dp)) {
				OutlinedButton(
					onClick = { onClickAdd() },
					shape = RoundedCornerShape(14.dp)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_add),
						contentDescription = "",
						modifier = Modifier.size(14.dp)
					)
					Spacer(modifier.width(8.dp))
					Text(text = "Add")
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentMethodList(
	modifier: Modifier = Modifier,
	paymentMethods: List<UiPaymentMethod>,
	selectedPaymentMethod: UiPaymentMethod?,
	onSelectPayment: (UiPaymentMethod) -> Unit
){
	var expanded by remember { mutableStateOf(false) }

	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_credit_card),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary,
				modifier = Modifier.scale(0.8f)
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = "Payment Method",
				style = MaterialTheme.typography.titleMedium
			)
		}
		Spacer(Modifier.height(8.dp))
		ExposedDropdownMenuBox(
			expanded = expanded,
			onExpandedChange = { expanded = it },
			) {
			TextField(
				modifier = Modifier
					.menuAnchor(MenuAnchorType.PrimaryNotEditable)
					.fillMaxWidth(),
				value = selectedPaymentMethod?.name ?: "None",
				onValueChange = {},
				readOnly = true,
				textStyle = MaterialTheme.typography.bodyMedium,
				singleLine = true,
				trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
				colors = ExposedDropdownMenuDefaults.textFieldColors(
					unfocusedIndicatorColor = Color.Transparent,
					unfocusedContainerColor = Color.Transparent,
					focusedIndicatorColor = Color.Transparent,
					focusedContainerColor = Color.Transparent
				),
			)
			ExposedDropdownMenu(
				expanded = expanded,
				onDismissRequest = { expanded = false },
			) {
				paymentMethods.forEach { option ->
					DropdownMenuItem(
						text = {
							Row (verticalAlignment = Alignment.CenterVertically){
								Image(
									painter = rememberAsyncImagePainter(model = option.logo),
									contentDescription = "",
									modifier = Modifier
										.height(16.dp)
										.width(40.dp)
								)
								Spacer(Modifier.width(8.dp))
								Text(
									text = option.name,
									style = MaterialTheme.typography.bodyMedium)
							}
						},
						onClick = {
							expanded = false
							onSelectPayment(option)
						},
						contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
					)
				}
			}
		}
	}
}

@Composable
private fun CheckoutSummary(
	modifier: Modifier = Modifier,
	checkoutData: UiCheckout
){
	Column(
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
	) {
		Column{
			Text(
				text = "Order Summary (${checkoutData.items.size} items)",
				style = MaterialTheme.typography.titleMedium
			)
		}
		Spacer(Modifier.height(8.dp))
		Column {
			Row (
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			){
				Text(
					text = stringResource(R.string.subTotal),
					style = MaterialTheme.typography.bodyMedium
				)
				Text(
					text = checkoutData.subTotal,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					)
				)
			}
			if (checkoutData.couponDiscount != null) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = stringResource(R.string.couponCodeDiscount),
						style = MaterialTheme.typography.bodyMedium
					)
					Text(
						text = checkoutData.couponDiscount,
						style = MaterialTheme.typography.bodyMedium.copy(
							fontWeight = FontWeight.W500
						)
					)
				}
			}
			if (checkoutData.shippingFee != null) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = stringResource(R.string.shippingFee),
						style = MaterialTheme.typography.bodyMedium
					)
					Text(
						text = checkoutData.shippingFee,
						style = MaterialTheme.typography.bodyMedium.copy(
							fontWeight = FontWeight.W500
						)
					)
				}
			}
			Row (
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			){
				Text(
					text = stringResource(R.string.totalAmount),
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					)
				)
				Text(
					text = checkoutData.totalAmount,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					)
				)
			}
		}
	}
}

@Composable
private fun CheckoutItem(
	modifier: Modifier = Modifier,
	cartProduct: UiCartProduct
){
	Column(
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceEvenly,
			verticalAlignment = Alignment.Top
		) {
			Image(
				painter = rememberAsyncImagePainter(model = cartProduct.image),
				modifier = Modifier
					.width(100.dp)
					.height(120.dp)
					.clip(RoundedCornerShape(14.dp)),
				contentDescription = "",
				contentScale = ContentScale.Crop
			)
			Column(modifier = Modifier.padding(start = 8.dp)) {
				Text(
					text = cartProduct.name,
					style = MaterialTheme.typography.bodyLarge,
					maxLines = 2,
				)
				Row (verticalAlignment = Alignment.CenterVertically){
					Text(
						text = cartProduct.activePrice,
						style = MaterialTheme.typography.bodyMedium.copy(
							fontWeight = FontWeight.W500
						)
					)
					Spacer(Modifier.width(4.dp))
					if (cartProduct.percentageDiscount < 0) {
						Text(
							text = cartProduct.price,
							style = MaterialTheme.typography.bodySmall.copy(
								textDecoration = TextDecoration.LineThrough,
								color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
							)
						)
					}
				}
				if (cartProduct.color !=null) {
					Row {
						Text(
							text = "Color:",
							style = MaterialTheme.typography.bodyMedium.copy(
								color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
							)
						)
						Spacer(Modifier.width(4.dp))
						Text(
							text = cartProduct.color,
							style = MaterialTheme.typography.bodyMedium
						)
					}
				}
				if (cartProduct.size != null) {
					Row {
						Text(
							text = "Size:",
							style = MaterialTheme.typography.bodyMedium.copy(
								color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
							)
						)
						Spacer(Modifier.width(4.dp))
						Text(
							text = cartProduct.size,
							style = MaterialTheme.typography.bodyMedium
						)
					}
				}
				Row {
					Text(
						text = "Qty:",
						style = MaterialTheme.typography.bodyMedium.copy(
							color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
						)
					)
					Spacer(Modifier.width(4.dp))
					Text(
						text = cartProduct.quantity.toString(),
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
		}
		HorizontalDivider(thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
		Column {
			Row (
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			){
				Text(
					text = stringResource(R.string.netAmount),
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
					)
				)
				Text(
					text = "${cartProduct.activePrice} x ${cartProduct.quantity}",
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
					)
				)
				Text(
					text = cartProduct.totalPrice,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
					)
				)
			}
			Row (
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			){
				Text(
					text = stringResource(R.string.total),
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
					)
				)
				Text(
					text = cartProduct.totalPrice,
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
	}
}

@Composable
private fun CheckoutBottomBar(
	modifier: Modifier = Modifier,
	onPlaceOrder: () -> Unit,
	isLoading: Boolean
){
	BottomAppBar (
		containerColor = MaterialTheme.colorScheme.surface,
		modifier = Modifier.shadow(elevation = 8.dp)
	){
		Row (
			modifier = modifier
				.fillMaxWidth(),
		){
			ButtonWithProgressIndicator(
				onClick = { onPlaceOrder() },
				isLoading = isLoading,
				modifier = Modifier.fillMaxWidth(),
				shape = RoundedCornerShape(16.dp),
				progressIndicatorModifier = Modifier.scale(0.8f),
			) {
				Text(text = stringResource(R.string.placeorder))
			}
		}
	}
}


private fun Context.getActivity():Activity? =when(this){
	is Activity -> this
	is ContextWrapper -> baseContext.getActivity()
	else -> null
}