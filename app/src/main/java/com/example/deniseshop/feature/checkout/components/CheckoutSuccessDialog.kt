package com.example.deniseshop.feature.checkout.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R

@Composable
fun CheckoutSuccessDialog (
	message: String,
	onDismiss: () -> Unit,
	onConfirm: () ->Unit
){
	AlertDialog(
		onDismissRequest = {},
		dismissButton = {},
		confirmButton = {
			Button(
				onClick = {
					onConfirm()
					onDismiss()
				}
			) {
				Text(text = stringResource(R.string.continue_shopping))
			}
		},
		icon = {
			Icon(
				painter = painterResource(R.drawable.ic_done),
				contentDescription = ""
			)
		},
		title = {
			Text(
				text = stringResource(R.string.order_placed_successfully),
			)
		},
		text = {
			Column {
				Text(
					text = stringResource(R.string.thank_you_for_shopping),
					style = MaterialTheme.typography.bodyLarge
				)
				Spacer(Modifier.height(8.dp))
				Text(
					text = message,
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
	)
}