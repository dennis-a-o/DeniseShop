package com.example.deniseshop.feature.cart.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R

@Composable
fun CartBottomBar(
	onClearAllClick: () -> Unit,
	onCheckoutClick: () -> Unit,
	modifier: Modifier = Modifier,
){
	var dialogState by remember { mutableStateOf(false) }

	if (dialogState){
		AlertDialog(
			onDismissRequest = { dialogState = false },
			confirmButton = {
				Button(
					onClick = {
						dialogState = false;
						onClearAllClick()
					}
				) {
					Text( stringResource(R.string.ok))
				}
			},
			dismissButton = {
				OutlinedButton(
					onClick = { dialogState = false }
				) {
					Text( stringResource(R.string.cancel))
				}
			},
			title = { Text(text = stringResource(R.string.confirm) ) },
			text = { Text(text = stringResource(R.string.confirmClearCartText)) }
		)
	}

	BottomAppBar (
		modifier = Modifier
			.shadow(elevation = 8.dp),
		containerColor = MaterialTheme.colorScheme.surface,
	){
		Row(
			modifier = modifier
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceEvenly,
			verticalAlignment = Alignment.CenterVertically
		) {
			Spacer(Modifier.width(8.dp))
			OutlinedButton(
				onClick = { dialogState = true },
				modifier = Modifier
					.weight(50f),
				shape = RoundedCornerShape(16.dp)
			) {
				Text(text = stringResource(R.string.clearALl))
			}
			Spacer(Modifier.width(8.dp))
			Button(
				onClick = onCheckoutClick,
				modifier = Modifier
					.weight(50f),
				shape = RoundedCornerShape(16.dp)
			) {
				Text(text = stringResource(R.string.checkout))
			}
			Spacer(Modifier.width(8.dp))
		}
	}
}