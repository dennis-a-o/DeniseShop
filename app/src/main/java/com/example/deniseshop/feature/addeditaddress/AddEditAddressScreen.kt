package com.example.deniseshop.feature.addeditaddress

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.AddressType
import com.example.deniseshop.core.presentation.components.ButtonWithProgressIndicator
import com.example.deniseshop.core.presentation.components.DeniseShopTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAddressScreen(
	viewModel: AddEditAddressViewModel,
	onBackClick: () -> Unit,
	onShowSnackBar: suspend (String,String?) -> Boolean,
) {
	val state by viewModel.state.collectAsState()

	var errorText by remember { mutableStateOf<String?>(null) }

	val context = LocalContext.current

	errorText = if (state.error != null){
		state.error?.asString()
	} else null

	if (state.success != null){
		Toast.makeText(context, state.success, Toast.LENGTH_LONG).show()
		viewModel.onEvent(AddEditAddressEvent.ClearMessage)
	}

	LaunchedEffect(errorText) {
		errorText?.let {
			onShowSnackBar(it, null)
			viewModel.onEvent(AddEditAddressEvent.ClearError)
		}
	}

	Column(
		modifier = Modifier
			.windowInsetsPadding(WindowInsets.navigationBars)
			.fillMaxSize()
	) {
		TopAppBar(
			title = {
				Text(
					text = stringResource(
						id = if (state.theAddress != null){
							R.string.edit_address
						}else{
							R.string.add_address
						}
					)
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
		AddressForm(
			state = state,
			onEvent = {
				viewModel.onEvent(it)
			}
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddressForm(
	state: AddEditAddressState,
	onEvent:(AddEditAddressEvent) -> Unit,
) {
	var countryListExpanded by remember { mutableStateOf(false) }
	var typeExpanded by remember { mutableStateOf(false) }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		DeniseShopTextField(
			text = state.name,
			onValueChange = {
				onEvent(AddEditAddressEvent.NameChange(it))
			},
			placeholder = stringResource(R.string.name),
			isError = state.nameError != null,
			errorMessage = state.nameError?.asString(),
			imeAction = ImeAction.Next
		)
		DeniseShopTextField(
			text = state.email,
			onValueChange = {
				onEvent(AddEditAddressEvent.EmailChange(it))
			},
			placeholder = stringResource(R.string.email),
			isError = state.emailError != null,
			errorMessage = state.emailError?.asString(),
			keyboardType = KeyboardType.Email,
			imeAction = ImeAction.Next
		)
		DeniseShopTextField(
			text = state.phone,
			placeholder = stringResource(R.string.mobile_number),
			onValueChange = {
				onEvent(AddEditAddressEvent.PhoneChange(it))
			},
			isError = state.phoneError != null,
			errorMessage = state.phoneError?.asString(),
			keyboardType = KeyboardType.Phone,
			imeAction = ImeAction.Next
		)
		ExposedDropdownMenuBox(
			expanded = countryListExpanded,
			onExpandedChange = { countryListExpanded = it },
		) {
			DeniseShopTextField(
				modifier = Modifier
					.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
					.fillMaxWidth(),
				text = state.country,
				placeholder = stringResource(R.string.country),
				onValueChange = {
					onEvent(AddEditAddressEvent.CountryChange(it))
				},
				isError = state.countryError != null,
				errorMessage = state.countryError?.asString(),
				trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryListExpanded) },
				imeAction = ImeAction.Next
			)
			ExposedDropdownMenu(
				expanded = countryListExpanded,
				onDismissRequest = { countryListExpanded = false },
			) {
				state.countries.forEach { option ->
					DropdownMenuItem(
						text = {
							Text(
								option,
								style = MaterialTheme.typography.bodyMedium
							)
						},
						leadingIcon = {
							Icon(
								painter = painterResource(R.drawable.ic_credit_card),
								contentDescription = ""
							)
						},
						onClick = {
							onEvent(AddEditAddressEvent.CountryChange(option))
							countryListExpanded = false
						},
						contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
					)
				}
			}
		}
		DeniseShopTextField(
			text = state.state,
			placeholder = stringResource(R.string.state),
			onValueChange = {
				onEvent(AddEditAddressEvent.StateChange(it))
			},
			isError = state.stateError != null,
			errorMessage = state.stateError?.asString(),
			imeAction = ImeAction.Next
		)
		DeniseShopTextField(
			text = state.city,
			placeholder = stringResource(R.string.city),
			onValueChange = {
				onEvent(AddEditAddressEvent.CityChange(it))
			},
			isError = state.cityError != null,
			errorMessage = state.cityError?.asString(),
			imeAction = ImeAction.Next
		)
		DeniseShopTextField(
			text = state.address,
			placeholder = stringResource(R.string.address),
			onValueChange = {
				onEvent(AddEditAddressEvent.AddressChange(it))
			},
			isError = state.addressError != null,
			errorMessage = state.addressError?.asString(),
			imeAction = ImeAction.Next
		)
		DeniseShopTextField(
			text = state.zipCode,
			placeholder = stringResource(R.string.zip_code),
			onValueChange = {
				onEvent(AddEditAddressEvent.ZipCodeChange(it))
			},
			isError = state.zipCodeError != null,
			errorMessage = state.zipCodeError?.asString(),
			keyboardType = KeyboardType.Number,
			imeAction = ImeAction.Next
		)
		ExposedDropdownMenuBox(
			expanded = typeExpanded,
			onExpandedChange = { typeExpanded = it },
		) {
			DeniseShopTextField(
				modifier = Modifier
					.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
					.fillMaxWidth(),
				text = state.type.name,
				placeholder = stringResource(R.string.type),
				readOnly = true,
				onValueChange = {},
				isError = state.typeError != null,
				errorMessage = state.typeError?.asString(),
				trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
			)
			ExposedDropdownMenu(
				expanded = typeExpanded,
				onDismissRequest = { typeExpanded = false },
			) {
				AddressType.entries.forEach { addressType ->
					DropdownMenuItem(
						text = {
							Text(
								text = addressType.name,
								style = MaterialTheme.typography.bodyMedium
							)
						},
						onClick = {
							onEvent(AddEditAddressEvent.AddressTypeChange(addressType))
							typeExpanded = false
						},
						contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
					)
				}
			}
		}
		AddressSetDefaultCheckBox(
			state = state,
			onEvent = { onEvent(it) }
		)
		ButtonWithProgressIndicator(
			onClick = { onEvent(AddEditAddressEvent.Submit) },
			modifier = Modifier.fillMaxWidth(),
			isLoading = state.isLoading,
			shape = RoundedCornerShape(16.dp),
			progressIndicatorModifier = Modifier.scale(0.8f)
		) {
			if (state.theAddress != null) {
				Text(text = stringResource(R.string.update))
			} else {
				Text(text = stringResource(R.string.save))
			}

		}
	}
}

@Composable
private fun AddressSetDefaultCheckBox(
	state: AddEditAddressState,
	onEvent: (AddEditAddressEvent) -> Unit
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
			text = stringResource(R.string.set_as_default_address),
			style = MaterialTheme.typography.bodyMedium
		)
		Switch(
			checked = state.default,
			onCheckedChange = { onEvent(AddEditAddressEvent.IsDefaultChange(it)) },
			modifier = Modifier.scale(0.8f),
			colors = SwitchDefaults.colors(
				uncheckedBorderColor = Color.Transparent,
				checkedBorderColor = Color.Transparent
			),
		)
	}
}