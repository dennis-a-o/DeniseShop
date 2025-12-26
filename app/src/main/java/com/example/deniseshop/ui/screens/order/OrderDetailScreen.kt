package com.example.deniseshop.ui.screens.order

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.R
import com.example.deniseshop.common.state.DownloadState
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.ui.components.ButtonWithProgressIndicator
import com.example.deniseshop.ui.components.DeniseShopTextField
import com.example.deniseshop.ui.components.RatingStar
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.models.ReviewFormState
import com.example.deniseshop.ui.models.UiOrderAddress
import com.example.deniseshop.ui.models.UiOrderDetail
import com.example.deniseshop.ui.models.UiOrderProduct
import com.example.deniseshop.ui.screens.order.viewModels.OrderDetailViewModel
import com.example.deniseshop.ui.screens.order.viewModels.ReviewFormEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
	onNavigateUp: () -> Unit,
	viewModel: OrderDetailViewModel
){
	val snackBarHostState = remember { SnackbarHostState() }
	val coroutineScope = rememberCoroutineScope()
	val context = LocalContext.current
	var allGranted by remember { mutableStateOf(false) }

	val permissions = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
		arrayOf(
			Manifest.permission.MANAGE_EXTERNAL_STORAGE,
			Manifest.permission.POST_NOTIFICATIONS
		)
	}else{
		arrayOf(
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.POST_NOTIFICATIONS
		)
	}

	val permissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestMultiplePermissions()
	) { permissionsMap ->
		 allGranted = permissionsMap.values.all { it }
	}

	val hasPermission = permissions.all{
		ContextCompat.checkSelfPermission(context, it) == android.content.pm.PackageManager.PERMISSION_GRANTED
	}

	val orderDetailState = viewModel.orderDetailState.collectAsState()
	val reviewFormState = viewModel.reviewFormState.collectAsState()
	val isReviewFormOpen = viewModel.isReviewFormVisible.collectAsState()
	val downloadInvoiceState = viewModel.downloadInvoiceState.collectAsState()
	val downloadItemState  = viewModel.downloadItemState.collectAsState()

	LaunchedEffect(
		key1 = reviewFormState.value.isError,
		key2 = downloadInvoiceState.value,
		key3 = downloadItemState.value
	) {
		if (reviewFormState.value.isError){
			coroutineScope.launch {
				snackBarHostState.showSnackbar(
					message = reviewFormState.value.message,
					duration = SnackbarDuration.Long,
				)
			}
			viewModel.onEvent(ReviewFormEvent.ResetMessage)
		}
		if (downloadInvoiceState.value is DownloadState.Error){
			coroutineScope.launch {
				snackBarHostState.showSnackbar(
					message = (downloadInvoiceState.value as DownloadState.Error).message,
					duration = SnackbarDuration.Long,
				)
			}
		}

		if (downloadItemState.value is DownloadState.Error ){
			coroutineScope.launch {
				snackBarHostState.showSnackbar(
					message = (downloadItemState.value as DownloadState.Error).message,
					duration = SnackbarDuration.Long,
				)
			}
		}
	}

	LaunchedEffect(
		key1 = reviewFormState.value.isSuccess,
		key2 = downloadInvoiceState.value,
		key3 = downloadItemState.value
	) {
		if (reviewFormState.value.isSuccess){
			Toast.makeText(context, reviewFormState.value.message, Toast.LENGTH_LONG).show()
			viewModel.onEvent(ReviewFormEvent.ResetMessage)
		}

		if (downloadInvoiceState.value is DownloadState.Success){
			Toast.makeText(context, (downloadInvoiceState.value as DownloadState.Success<String>).result, Toast.LENGTH_LONG).show()
		}

		if (downloadItemState.value is DownloadState.Success){
			Toast.makeText(context, (downloadItemState.value as DownloadState.Success<String>).result, Toast.LENGTH_LONG).show()
		}
	}

	LaunchedEffect(hasPermission) {
		if (!hasPermission){
			permissionLauncher.launch(permissions)
		}
		allGranted = hasPermission
	}

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.orderDetail)) },
				modifier = Modifier.shadow(elevation = 1.dp),
				navigationIcon = {
					IconButton(onClick = { onNavigateUp() }) {
						Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = "" )
					}
				}
			)
		},
		snackbarHost = {
			SnackbarHost(hostState = snackBarHostState)
		}
	){ paddingValues ->
		Box(
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize()
		){
			when(orderDetailState.value){
				is ScreenState.Error -> {
					ErrorUi(onErrorAction = { viewModel.onRetry() })
				}
				is ScreenState.Loading -> {
					LoadingUi()
				}
				is ScreenState.Success -> {
					OrderDetailScreen(
						orderDetail = (orderDetailState.value as ScreenState.Success<UiOrderDetail>).uiData,
						onClickCreateReview = { viewModel.onEvent(ReviewFormEvent.Open(it)) },
						downloadInvoiceState = downloadInvoiceState.value,
						downloadItemState = downloadItemState.value,
						onDownloadItem = { id ->
							if (allGranted) {
								viewModel.onDownloadItem(id)
							}else{
								permissionLauncher.launch(permissions)
							}
						},
						onDownloadInvoice = { id ->
							if (allGranted) {
								viewModel.onDownloadInvoice(id)
							}else{
								permissionLauncher.launch(permissions)
							}
						}
					)
				}
			}
		}
		ReviewFormModal(
			isReviewFormOpen = isReviewFormOpen.value,
			reviewFormState = reviewFormState.value,
			onEvent = { viewModel.onEvent(it) }
		)
	}
}

@Composable
private fun OrderDetailScreen(
	orderDetail: UiOrderDetail,
	onClickCreateReview: (Long) -> Unit,
	downloadInvoiceState: DownloadState<String>,
	downloadItemState: DownloadState<String>,
	onDownloadItem: (Long) -> Unit,
	onDownloadInvoice: (Long) -> Unit
){
	Column (
		modifier = Modifier
			.verticalScroll(rememberScrollState())
			.padding(horizontal = 8.dp)
			.fillMaxSize()
	){
		Spacer(Modifier.height(8.dp))
		OrderInformation(orderDetail = orderDetail)
		Spacer(Modifier.height(8.dp))
		orderDetail.orderAddress?.let {
			CustomerAddress(address = it)
			Spacer(Modifier.height(8.dp))
		}
		OrderProducts(
			products = orderDetail.products,
			onClickReview = {
				onClickCreateReview(it)
			},
			onClickDownload = {
				onDownloadItem(it)
			},
			downloadState = downloadItemState
		)
		Spacer(Modifier.height(8.dp))
		if (orderDetail.containPhysicalProduct) {
			ShippingInformation(orderDetail = orderDetail)
			Spacer(Modifier.height(8.dp))
		}
		OrderInvoice(
			onClickDownload = {
				onDownloadInvoice(orderDetail.id)
			},
			downloadInvoiceState = downloadInvoiceState
		)
		Spacer(Modifier.height(8.dp))
	}
}

@Composable
private fun OrderInformation(
	modifier: Modifier = Modifier,
	orderDetail: UiOrderDetail
){
	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
			.fillMaxWidth()
	){
		Text(
			text = "Order Information",
			style = MaterialTheme.typography.titleMedium
		)
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Order:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = orderDetail.code,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Placed on:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = orderDetail.date.substring(0,10),
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Order status:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = orderDetail.status,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Payment method:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = orderDetail.paymentMethod,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Payment status:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = orderDetail.paymentStatus,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "No of items:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = orderDetail.products.size.toString(),
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Total amount:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = orderDetail.amount,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Tax:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = orderDetail.tax,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Discount:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = orderDetail.discount,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Shipping fee:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = orderDetail.shippingFee,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
	}
}

@Composable
private fun OrderProducts(
	modifier: Modifier = Modifier,
	products: List<UiOrderProduct>,
	onClickReview: (Long) -> Unit,
	onClickDownload: (Long) -> Unit,
	downloadState: DownloadState<String>
){
	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
			.fillMaxWidth()
	){
		Text(
			text = stringResource(R.string.products),
			style = MaterialTheme.typography.titleMedium
		)
		Spacer(Modifier.height(8.dp))
		products.forEachIndexed{ index,product ->
			OrderItem(
				product = product,
				onClickReview = { onClickReview(it) },
				onClickDownload = { onClickDownload(it) },
				downloadState = downloadState
			)
			if(index != (products.size - 1)){
				HorizontalDivider(thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
			}
		}
	}
}
@Composable
private fun OrderItem(
	modifier: Modifier = Modifier,
	product: UiOrderProduct,
	onClickReview: (Long) -> Unit,
	onClickDownload: (Long) -> Unit,
	downloadState: DownloadState<String>
){
	Column (modifier = modifier){
		Row (verticalAlignment = Alignment.Top) {
			Image(
				painter = rememberAsyncImagePainter(model = product.image),
				contentDescription = "",
				modifier = Modifier
					.width(80.dp)
					.height(80.dp)
					.clip(RoundedCornerShape(14.dp)),
				contentScale = ContentScale.Crop
			)
			Spacer(Modifier.width(8.dp))
			Column {
				Text(
					text = product.name,
					style = MaterialTheme.typography.bodyLarge,
					maxLines = 2
				)
				Text(
					text = "Qty: ${product.quantity}",
					style = MaterialTheme.typography.bodyMedium
				)
				Text(
					text = product.price,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					),
					maxLines = 1
				)
			}
		}
		Spacer(Modifier.height(8.dp))
		Row (horizontalArrangement = Arrangement.SpaceBetween){
			if(!(product.rated)) {
				OutlinedButton(
					onClick = {
						onClickReview(product.id)
					},
					modifier = Modifier.fillMaxWidth(0.5f),
					shape = RoundedCornerShape(16.dp),
					border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
				) {
					Row(verticalAlignment = Alignment.CenterVertically) {
						Icon(
							painter = painterResource(R.drawable.ic_rate_review_outlined),
							contentDescription = "",
							modifier = Modifier.scale(0.8f)
						)
						Spacer(Modifier.width(4.dp))
						Text(text = "Write Review")
					}
				}
				Spacer(Modifier.width(8.dp))
			}
			if (product.downloadable) {
				Button(
					onClick = {
						if (downloadState !is DownloadState.Downloading) {
							onClickDownload(product.id)
						}
					},
					modifier = Modifier.fillMaxWidth(),
					shape = RoundedCornerShape(16.dp)
				) {
					if (downloadState is DownloadState.Loading){
						CircularProgressIndicator(
							modifier = Modifier.size(14.dp),
							trackColor = MaterialTheme.colorScheme.surfaceContainerLowest
						)
					}else {
						Text(text = "Download")
					}
				}
			}
		}
	}
}

@Composable
private fun OrderInvoice(
	modifier: Modifier = Modifier,
	onClickDownload: () -> Unit,
	downloadInvoiceState: DownloadState<String>
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				if (downloadInvoiceState !is DownloadState.Downloading) {
					onClickDownload()
				}
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			if (downloadInvoiceState is DownloadState.Loading) {
				CircularProgressIndicator(modifier = Modifier.size(16.dp))
			}else{
				Icon(
					painter = painterResource(R.drawable.ic_receipt),
					contentDescription = "",
					tint = MaterialTheme.colorScheme.primary
				)
			}
			Spacer(Modifier.width(16.dp))
			Text(
				text = "Download Invoice",
				style = MaterialTheme.typography.bodyMedium
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "" ,
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun CustomerAddress(
	modifier: Modifier = Modifier,
	address: UiOrderAddress
){
	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
			.fillMaxWidth()
	) {
		Text(
			text = "Customer",
			style = MaterialTheme.typography.titleMedium
		)
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Name:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = address.name,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Phone:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = address.phone,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Email:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = address.email,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Address:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = address.address,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
	}
}

@Composable
private fun ShippingInformation(
	modifier: Modifier = Modifier,
	orderDetail: UiOrderDetail
){
	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
			.fillMaxWidth()
	){
		Text(
			text = "Shipping Information",
			style = MaterialTheme.typography.titleMedium
		)
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Shipping status:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = orderDetail.shippingStatus.toString(),
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = "Shipping:",
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = orderDetail.shipping.toString(),
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewFormModal(
	isReviewFormOpen: Boolean,
	reviewFormState: ReviewFormState,
	onEvent: (ReviewFormEvent) -> Unit
){
	val skipPartiallyExpanded by rememberSaveable { mutableStateOf(true) }
	val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

	if (isReviewFormOpen){
		ModalBottomSheet(
			onDismissRequest = { onEvent(ReviewFormEvent.Close) },
			sheetState = bottomSheetState,
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.heightIn(max = 500.dp)
					.padding(8.dp)
			){
				Row (
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.Center
				){
					Text(
						text = "Product Review",
						style = MaterialTheme.typography.titleLarge
					)
				}
				Spacer(Modifier.height(16.dp))
				Column {
					RatingStar(
						rating = reviewFormState.rating,
						onStartClick = { onEvent(ReviewFormEvent.StarChanged(it)) },
						isIndicator = true,
						starModifier = Modifier.size(32.dp)
					)
					if (reviewFormState.ratingError != null){
						Text(
							text = reviewFormState.ratingError,
							style = MaterialTheme.typography.bodySmall.copy(
								color = MaterialTheme.colorScheme.error
							)
						)
					}
				}
				Spacer(Modifier.height(8.dp))

				DeniseShopTextField(
					placeholder = "Review",
					text = reviewFormState.review,
					onValueChange = { onEvent(ReviewFormEvent.ReviewChanged(it)) },
					isError = reviewFormState.reviewError != null,
					errorMessage = reviewFormState.reviewError,
					maxLine = 8,
					minLine = 4
				)
				Spacer(Modifier.height(8.dp))
				ButtonWithProgressIndicator(
					onClick = { onEvent(ReviewFormEvent.Submit) },
					modifier = Modifier.fillMaxWidth(),
					shape = RoundedCornerShape(16.dp),
					isLoading = reviewFormState.isLoading,
					progressIndicatorModifier = Modifier.scale(0.8f)
				) {
					Text(text = "Send")
				}
				Spacer(Modifier.height(8.dp))
			}
		}
	}
}


