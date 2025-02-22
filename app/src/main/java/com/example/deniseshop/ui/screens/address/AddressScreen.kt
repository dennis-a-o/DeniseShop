package com.example.deniseshop.ui.screens.address

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.example.deniseshop.R
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.models.UiAddress
import com.example.deniseshop.ui.screens.address.viewModels.AddressViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
	onNavigate: (UiAddress, NavOptions?) -> Unit,
	onNavigateUp: () -> Unit,
	viewModel: AddressViewModel = hiltViewModel()
){
	val addressState = viewModel.addressState.collectAsState()
	val actionState = viewModel.actionState.collectAsState()

	val context = LocalContext.current

	LaunchedEffect(actionState.value) {
		if (actionState.value.isError || actionState.value.isSuccess) {
			Toast.makeText(context, actionState.value.message, Toast.LENGTH_LONG).show()
			viewModel.resetActionState()
		}
	}
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.myAddress)) },
				modifier = Modifier.shadow(elevation = 1.dp),
				navigationIcon = {
					IconButton(onClick = { onNavigateUp() }) {
						Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "" )
					}
				}
			)
		},
	){ paddingValues ->
		Box(
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize()
		){
			when(addressState.value){
				is ScreenState.Error -> {
					ErrorUi(onErrorAction = { viewModel.onRetry() })
				}
				is ScreenState.Loading -> {
					LoadingUi()
				}
				is ScreenState.Success -> {
					AddressScreen(
						addresses = (addressState.value as ScreenState.Success<List<UiAddress>>).uiData,
						onClickAdd = {
							onNavigate(UiAddress(),null)
						},
						onClickEdit = {
							onNavigate(it,null)
						},
						onCheckDefault = { viewModel.onSetAddressDefault(it) },
						onClickDelete = { viewModel.onRemoveAddress(it) }
					)
				}
			}
		}
	}
}

@Composable
private fun AddressScreen(
	addresses : List<UiAddress>,
	onClickAdd: () -> Unit,
	onClickEdit: (UiAddress) -> Unit,
	onCheckDefault: (Long) -> Unit,
	onClickDelete: (Long) -> Unit
){
	Box (
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = 8.dp)
	){
		LazyColumn {
			item { Spacer(Modifier.height(8.dp)) }
			addresses.forEach{ address ->
				item {
					AddressItem(
						address = address,
						onClickEdit = { onClickEdit(address) },
						onCheckDefault = { onCheckDefault(it) },
						onClickDelete = { onClickDelete(it) }
					)
					Spacer(Modifier.height(8.dp))
				}
			}
		}
		FloatingActionButton(
			onClick = { onClickAdd() },
			modifier = Modifier
				.padding(end = 16.dp, bottom = 16.dp)
				.align(Alignment.BottomEnd)

		) {
			Icon(painter = painterResource(R.drawable.ic_add), contentDescription = "")
		}
	}
}

@Composable
private fun AddressItem(
	address: UiAddress,
	onClickEdit: (Long) -> Unit,
	onCheckDefault: (Long) -> Unit,
	onClickDelete: (Long) -> Unit,
){
	var checkedState by rememberSaveable { mutableStateOf(false) }
	Column(
		modifier = Modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
			.fillMaxWidth()
	){
		Text(
			text = address.name,
			modifier = Modifier.fillMaxWidth(),
			style = MaterialTheme.typography.titleMedium.copy(
				color = MaterialTheme.colorScheme.onBackground,
			),
			maxLines = 1
		)
		Text(
			text = address.email,
			modifier = Modifier.fillMaxWidth(),
			style = MaterialTheme.typography.bodyMedium.copy(
				color = MaterialTheme.colorScheme.onBackground,
			),
			maxLines = 1
		)
		Text(
			text = address.phone,
			modifier = Modifier.fillMaxWidth(),
			style = MaterialTheme.typography.bodyMedium.copy(
				color = MaterialTheme.colorScheme.onBackground,
			),
			maxLines = 1
		)
		Text(
			text = "${address.address}, ${address.state}, ${address.city}, ${address.country}",
			style = MaterialTheme.typography.bodyMedium.copy(
				color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
			),
		)
		if (address.default){
			Spacer(Modifier.height(8.dp))
		}
		Row (
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		){
			Row(
				verticalAlignment = Alignment.CenterVertically,
			){
				if (address.default){
					Text(
						text = "Default",
						modifier = Modifier
							.background(
								color = MaterialTheme.colorScheme.inversePrimary,
								shape = RoundedCornerShape(16.dp)
							)
							.padding(horizontal = 8.dp),
						style = MaterialTheme.typography.bodySmall
					)
				} else {
					Checkbox(
						checked = checkedState,
						onCheckedChange = {
							checkedState = it
							onCheckDefault(address.id)
						},
						modifier = Modifier.scale(0.8f)
					)
					Text(
						text = stringResource(R.string.setAsDefault),
						style = MaterialTheme.typography.bodyMedium
					)
				}

			}
			Row (verticalAlignment = Alignment.CenterVertically){
				IconButton(
					onClick = { onClickEdit(address.id) },
					modifier = Modifier.size(20.dp)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_edit_outlined),
						contentDescription = "",
						tint = MaterialTheme.colorScheme.primary
					)
				}
				Spacer(Modifier.width(16.dp))
				IconButton(
					onClick = { onClickDelete(address.id) },
					modifier = Modifier.size(20.dp)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_delete),
						contentDescription = "",
						tint = MaterialTheme.colorScheme.primary
					)
				}
			}
		}
	}
}