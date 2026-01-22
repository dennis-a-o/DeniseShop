package com.example.deniseshop.feature.checkout.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.PaymentMethod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodSection(
	paymentMethods: List<PaymentMethod>,
	selectedPaymentMethod: PaymentMethod?,
	onSelectPayment: (PaymentMethod) -> Unit,
	modifier: Modifier = Modifier,
){
	var expanded by remember { mutableStateOf(false) }

	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(16.dp)
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_credit_card),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary,
				modifier = Modifier
					.size(16.dp)
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = stringResource(R.string.payment_method),
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
					.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
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
							Text(
								text = option.name,
								style = MaterialTheme.typography.bodyMedium
							)
						},
						leadingIcon = {
							Image(
								painter = rememberAsyncImagePainter(model = option.logo),
								contentDescription = "",
								modifier = Modifier
									.height(16.dp)
									.width(40.dp)
							)
						},
						onClick = {
							onSelectPayment(option)
							expanded = false
						},
						contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
					)
				}
			}
		}
	}
}