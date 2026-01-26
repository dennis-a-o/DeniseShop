package com.example.deniseshop.feature.orderdetail

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.deniseshop.R
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.LoadingUi
import com.example.deniseshop.feature.orderdetail.components.CustomerAddressSection
import com.example.deniseshop.feature.orderdetail.components.InvoiceSection
import com.example.deniseshop.feature.orderdetail.components.OrderInformationSection
import com.example.deniseshop.feature.orderdetail.components.OrderItemsSection
import com.example.deniseshop.feature.orderdetail.components.ReviewFormBottomSheet
import com.example.deniseshop.feature.orderdetail.components.ShippingSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
	viewModel: OrderDetailViewModel,
	onBackClick: () -> Unit,
	onShowSnackBar: suspend (String, String?) -> Boolean,
) {
	val state by viewModel.state.collectAsState()
	val context = LocalContext.current

	var permissionsGranted by remember { mutableStateOf(false) }
	var errorText by remember { mutableStateOf<String?>(null) }

	errorText = if (state.error != null){
		state.error!!.asString()
	} else null

	LaunchedEffect(errorText) {
		errorText?.let {
			onShowSnackBar(it,null)
			viewModel.clearErrorSuccessState()
		}
	}

	if (state.success != null){
		Toast.makeText(context, state.success, Toast.LENGTH_LONG).show()
		viewModel.clearErrorSuccessState()
	}

	val permissions = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
		arrayOf(
			Manifest.permission.MANAGE_EXTERNAL_STORAGE,
			Manifest.permission.POST_NOTIFICATIONS,
		)
	}else{
		arrayOf(
			Manifest.permission.POST_NOTIFICATIONS,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
		)
	}

	val permissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestMultiplePermissions()
	) { permissionsMap ->
		permissionsGranted = permissionsMap.values.all { it }
	}

	val hasPermission = permissions.all{
		ContextCompat.checkSelfPermission(context, it) == android.content.pm.PackageManager.PERMISSION_GRANTED
	}

	LaunchedEffect(hasPermission) {
		permissionsGranted = hasPermission
	}

	var showReviewBottomSheetWithItemId by remember { mutableStateOf<Long?>(null) }

	showReviewBottomSheetWithItemId?.let { itemId ->
		ReviewFormBottomSheet(
			isLoading = state.isSubmittingReview,
			itemId = itemId,
			onDismiss = {
				showReviewBottomSheetWithItemId = null
			},
			onSubmit = { itemId, rating, review ->
				viewModel.onReview(itemId, rating, review)
			}
		)
	}

	Column(
		modifier = Modifier
			.windowInsetsPadding(WindowInsets.navigationBars)
			.fillMaxSize()
	) {
		TopAppBar(
			title = {
				Text(
					text = stringResource(R.string.orderDetail),
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
			}
		)
		PullToRefreshBox(
			isRefreshing = false,
			onRefresh = {
				viewModel.retry()
			},
			modifier = Modifier
				.fillMaxSize()
		) {
			if (state.isLoading){
				LoadingUi(
					modifier = Modifier
						.fillMaxSize()
				)
			}

			if (!state.isLoading && state.loadingError != null){
				ErrorUi(
					message = state.loadingError!!.asString(),
					onErrorAction = {
						viewModel.retry()
					}
				)
			}

			if (state.loadingError == null && state.orderDetail != null){
				OrderDetailScreen(
					state = state,
					onReviewClick = {
						showReviewBottomSheetWithItemId = it
					},
					onDownloadItemClick = {
						if (permissionsGranted) {
							viewModel.downloadOrderItem(it)
						}else{
							permissionLauncher.launch(permissions)
						}
					},
					onDownloadInvoiceClick = {
						if (permissionsGranted) {
							viewModel.downloadInvoice()
						}else{
							permissionLauncher.launch(permissions)
						}
					}
				)
			}
		}
	}
}

@Composable
private fun OrderDetailScreen(
	state: OrderDetailState,
	onDownloadItemClick: (Long) ->Unit,
	onDownloadInvoiceClick: () -> Unit,
	onReviewClick: (Long) -> Unit,
	modifier: Modifier = Modifier
){
	Column(
		modifier = modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		state.orderDetail?.let {
			OrderInformationSection(
				orderDetail = it
			)
		}
		state.orderDetail?.orderAddress?.let {
			CustomerAddressSection(
				address = it
			)
		}
		state.orderDetail?.let {
			OrderItemsSection(
				products = it.products,
				isDownloading = state.isDownloadingItem,
				onReviewClick = onReviewClick,
				onDownloadClick = onDownloadItemClick
			)
		}
		state.orderDetail?.let {
			ShippingSection(
				orderDetail = it
			)
		}
		InvoiceSection(
			isDownloading = state.isDownloadingInvoice,
			onDownloadClick = onDownloadInvoiceClick
		)
	}
}