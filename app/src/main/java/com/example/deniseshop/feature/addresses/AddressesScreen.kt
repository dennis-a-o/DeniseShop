package com.example.deniseshop.feature.addresses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Address
import com.example.deniseshop.core.presentation.ScreenState
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.LoadingUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressesScreen(
	viewModel: AddressesViewModel = hiltViewModel(),
	onBackClick: () -> Unit,
	onAddressClick: (Long) -> Unit,
	onShowSnackBar: suspend (String, String?) -> Boolean,
) {
	val state by viewModel.state.collectAsState()
	val error by viewModel.error.collectAsState()

	var errorText by remember { mutableStateOf<String?>(null) }

	errorText = if (error != null){
		error?.asString()
	} else null

	LaunchedEffect(errorText) {
		errorText?.let {
			onShowSnackBar(it, null)
			viewModel.clearError()
		}
	}

	Column(
		modifier = Modifier
			.windowInsetsPadding(WindowInsets.navigationBars)
			.fillMaxSize()
	) {
		TopAppBar(
			title = { Text(text = stringResource(R.string.addresses)) },
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
			when (state) {
				ScreenState.Loading -> {
					LoadingUi(
						modifier = Modifier
							.fillMaxSize()
					)
				}

				is ScreenState.Error -> {
					ErrorUi(
						onErrorAction = {
							viewModel.retry()
						},
					)
				}

				is ScreenState.Success -> {
					AddressesScreen(
						addresses = (state as ScreenState.Success<List<Address>>).data,
						onAddClick = {
							onAddressClick(0L)
						},
						onEditClick = onAddressClick,

						onDeleteClick = {
							viewModel.removeAddress(it)
						},
						onMakeDefault = {
							viewModel.setDefaultAddress(it)
						},
					)
				}
			}
		}
	}
}

@Composable
private fun AddressesScreen(
	addresses : List<Address>,
	onAddClick: () -> Unit,
	onEditClick: (Long) -> Unit,
	onMakeDefault: (Long) -> Unit,
	onDeleteClick: (Long) -> Unit
){
	Box (
		modifier = Modifier
			.fillMaxSize()
	){
		if (addresses.isNotEmpty()) {
			LazyColumn(
				contentPadding = PaddingValues(16.dp),
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				items(addresses.size) { index ->
					AddressItem(
						address = addresses[index],
						onEditClick = onEditClick,
						onDeleteClick = onDeleteClick,
						onMakeDefaultClick = onMakeDefault
					)
				}
			}
		}else{
			Text(
				text = stringResource(R.string.no_address),
				modifier = Modifier
					.align(Alignment.Center),
				style = MaterialTheme.typography.bodyLarge,
			)
		}
		FloatingActionButton(
			onClick = onAddClick,
			modifier = Modifier
				.padding(end = 16.dp, bottom = 16.dp)
				.align(Alignment.BottomEnd)

		) {
			Icon(
				painter = painterResource(R.drawable.ic_add),
				contentDescription = ""
			)
		}
	}
}