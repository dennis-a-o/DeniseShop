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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.ui.components.ButtonWithProgressIndicator
import com.example.deniseshop.ui.components.DeniseShopTextField
import com.example.deniseshop.ui.models.AddressFormState
import com.example.deniseshop.ui.screens.address.viewModels.AddressFormEvent
import com.example.deniseshop.ui.screens.address.viewModels.AddressFormViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressFormScreen(
	onNavigateUp: () -> Unit,
	viewModel: AddressFormViewModel,
){
	val isEditing= viewModel.isEditing.collectAsState()
	val addressFormState = viewModel.addressFormState.collectAsState()
	val countries = viewModel.countries.collectAsState()
	val actionState = viewModel.actionState.collectAsState()

	val snackBarHostState = remember { SnackbarHostState() }
	val coroutineScope = rememberCoroutineScope()
	val context = LocalContext.current

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
		topBar = {
			TopAppBar(
				title = { Text(text = if (isEditing.value) "Edit Address" else "Add Address") },
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
	) { paddingValues ->
		Box(
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize()
		) {
			AddressForm(
				addressFormState = addressFormState.value,
				onEvent = {
					viewModel.onEvent(it)
				},
				isEditing = isEditing.value,
				countries = countries.value
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddressForm(
	addressFormState: AddressFormState,
	onEvent:(AddressFormEvent) -> Unit,
	isEditing:Boolean,
	countries: List<String>
) {
	var countryListExpanded by remember { mutableStateOf(false) }
	var typeExpanded by remember { mutableStateOf(false) }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
			.padding(horizontal = 8.dp)
	) {
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			text = addressFormState.name,
			onValueChange = {
				onEvent(AddressFormEvent.NameChanged(it))
			},
			placeholder = "Name",
			isError = addressFormState.nameError != null,
			errorMessage = addressFormState.nameError,
			imeAction = ImeAction.Next
		)
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			text = addressFormState.email,
			onValueChange = {
				onEvent(AddressFormEvent.EmailChanged(it))
			},
			placeholder = "Email",
			isError = addressFormState.emailError != null,
			errorMessage = addressFormState.emailError,
			keyboardType = KeyboardType.Email,
			imeAction = ImeAction.Next
		)
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			text = addressFormState.phone,
			placeholder = "Mobile Number",
			onValueChange = {
				onEvent(AddressFormEvent.PhoneChanged(it))
			},
			isError = addressFormState.phoneError != null,
			errorMessage = addressFormState.phoneError,
			keyboardType = KeyboardType.Phone,
			imeAction = ImeAction.Next
		)
		Spacer(Modifier.height(8.dp))
		ExposedDropdownMenuBox(
			expanded = countryListExpanded,
			onExpandedChange = { countryListExpanded = it },
		) {
			DeniseShopTextField(
				modifier = Modifier
					.menuAnchor(MenuAnchorType.PrimaryNotEditable)
					.fillMaxWidth(),
				text = addressFormState.country,
				placeholder = "Country",
				onValueChange = {
					onEvent(AddressFormEvent.CountryChanged(it))
				},
				isError = addressFormState.countryError != null,
				errorMessage = addressFormState.countryError,
				trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryListExpanded) },
				imeAction = ImeAction.Next
			)
			ExposedDropdownMenu(
				expanded = countryListExpanded,
				onDismissRequest = { countryListExpanded = false },
			) {
				countries.forEach { option ->
					DropdownMenuItem(
						text = {
							Row(verticalAlignment = Alignment.CenterVertically) {
								Icon(
									painter = painterResource(R.drawable.ic_credit_card),
									contentDescription = ""
								)
								Spacer(Modifier.width(8.dp))
								Text(
									option,
									style = MaterialTheme.typography.bodyMedium
								)
							}
						},
						onClick = {
							onEvent(AddressFormEvent.CountryChanged(option))
							countryListExpanded = false
						},
						contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
					)
				}
			}
		}
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			text = addressFormState.state,
			placeholder = "State",
			onValueChange = {
				onEvent(AddressFormEvent.StateChanged(it))
			},
			isError = addressFormState.stateError != null,
			errorMessage = addressFormState.stateError,
			imeAction = ImeAction.Next
		)
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			text = addressFormState.city,
			placeholder = "City",
			onValueChange = {
				onEvent(AddressFormEvent.CityChanged(it))
			},
			isError = addressFormState.cityError != null,
			errorMessage = addressFormState.cityError,
			imeAction = ImeAction.Next
		)
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			text = addressFormState.address,
			placeholder = "Address",
			onValueChange = {
				onEvent(AddressFormEvent.AddressChanged(it))
			},
			isError = addressFormState.addressError != null,
			errorMessage = addressFormState.addressError,
			imeAction = ImeAction.Next
		)
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			text = addressFormState.zipCode,
			placeholder = "Zip Code",
			onValueChange = {
				onEvent(AddressFormEvent.ZipCodeChanged(it))
			},
			isError = addressFormState.zipCodeError != null,
			errorMessage = addressFormState.zipCodeError,
			keyboardType = KeyboardType.Number,
			imeAction = ImeAction.Next
		)
		Spacer(Modifier.height(8.dp))
		ExposedDropdownMenuBox(
			expanded = typeExpanded,
			onExpandedChange = { typeExpanded = it },
		) {
			DeniseShopTextField(
				modifier = Modifier
					.menuAnchor(MenuAnchorType.PrimaryNotEditable)
					.fillMaxWidth(),
				text = addressFormState.type,
				placeholder = "Type",
				readOnly = true,
				onValueChange = {},
				isError = addressFormState.typeError != null,
				errorMessage = addressFormState.typeError,
				trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
			)
			ExposedDropdownMenu(
				expanded = typeExpanded,
				onDismissRequest = { typeExpanded = false },
			) {
				DropdownMenuItem(
					text = {
						Text(
							text = "Home",
							style = MaterialTheme.typography.bodyMedium
						)
					},
					onClick = {
						onEvent(AddressFormEvent.AddressTypeChanged("home"))
						typeExpanded = false
					},
					contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
				)
				DropdownMenuItem(
					text = {
						Text(
							text = "Billing",
							style = MaterialTheme.typography.bodyMedium
						)
					},
					onClick = {
						onEvent(AddressFormEvent.AddressTypeChanged("billing"))
						typeExpanded = false
					},
					contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
				)
				DropdownMenuItem(
					text = {
						Text(
							text = "Office",
							style = MaterialTheme.typography.bodyMedium
						)
					},
					onClick = {
						onEvent(AddressFormEvent.AddressTypeChanged("office"))
						typeExpanded = false
					},
					contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
				)

				DropdownMenuItem(
					text = {
						Text(
							text = "Shipping",
							style = MaterialTheme.typography.bodyMedium
						)
					},
					onClick = {
						onEvent(AddressFormEvent.AddressTypeChanged("shipping"))
						typeExpanded = false
					},
					contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
				)
				DropdownMenuItem(
					text = {
						Text(
							text = "Other",
							style = MaterialTheme.typography.bodyMedium
						)
					},
					onClick = {
						onEvent(AddressFormEvent.AddressTypeChanged("other"))
						typeExpanded = false
					},
					contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
				)
			}
		}
		Spacer(Modifier.height(8.dp))
		AddressSetDefaultCheckBox(
			addressFormState = addressFormState,
			onEvent = { onEvent(it) }
		)
		Spacer(Modifier.height(8.dp))
		ButtonWithProgressIndicator(
			onClick = { onEvent(AddressFormEvent.Submit) },
			modifier = Modifier.fillMaxWidth(),
			isLoading = addressFormState.isLoading,
			shape = RoundedCornerShape(16.dp),
			progressIndicatorModifier = Modifier.scale(0.8f)
		) {
			if (isEditing) {
				Text(text = "Update")
			} else {
				Text(text = "Save")
			}

		}
		Spacer(Modifier.height(8.dp))
	}
}

@Composable
private fun AddressSetDefaultCheckBox(
	addressFormState: AddressFormState,
	onEvent: (AddressFormEvent) -> Unit
){
	Row (
		modifier = Modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(14.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	){
		Text(
			text = "Set as default address",
			style = MaterialTheme.typography.bodyMedium
		)
		Switch(
			checked = addressFormState.default,
			onCheckedChange = { onEvent(AddressFormEvent.IsDefaultChanged(it)) },
			modifier = Modifier.scale(0.8f),
			colors = SwitchDefaults.colors(
				uncheckedBorderColor = Color.Transparent,
				checkedBorderColor = Color.Transparent
			),
		)
	}
}
