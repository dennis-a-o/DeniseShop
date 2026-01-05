package com.example.deniseshop.feature.profile.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.deniseshop.R

@Composable
fun DeleteAccountDialog(
	onAccept: () -> Unit,
	onCancel: () -> Unit,
){
	AlertDialog(
		onDismissRequest = { onCancel() },
		confirmButton = {
			TextButton(
				onClick = onAccept
			) {
				Text(text = stringResource(R.string.delete))
			}
		},
		dismissButton = {
			TextButton(
				onClick = onCancel
			) {
				Text(text = stringResource(R.string.cancel))
			}
		},
		title = {
			Text(text = stringResource(R.string.deleteAccountDialogTitle))
		},
		text = {
			Text(text = stringResource(R.string.deleteAccountDialogText))
		}
	)
}