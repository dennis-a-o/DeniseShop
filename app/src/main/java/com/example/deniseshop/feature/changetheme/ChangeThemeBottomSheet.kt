package com.example.deniseshop.feature.changetheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeThemeBottomSheet(
	viewModel: ChangeThemeViewModel = hiltViewModel(),
	onDismiss: () -> Unit
) {
	val currentThemeMode by viewModel.themeMode.collectAsState()

	ModalBottomSheet(
		onDismissRequest = onDismiss,
		sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp),
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center
			) {
				Text(
					text = stringResource(R.string.changeTheme),
					style = MaterialTheme.typography.titleLarge
				)
			}
			Spacer(Modifier.height(32.dp))
			ThemeMode.entries.forEach { themeMode ->
				Row (
					modifier = Modifier
						.clickable {
							viewModel.setThemeMode(themeMode)
						}
						.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				){
					RadioButton(
						selected = themeMode == currentThemeMode,
						onClick = {})
					Text(
						text = themeMode.name,
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
		}
	}
}