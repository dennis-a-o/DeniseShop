package com.example.deniseshop.feature.changetheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
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

@Composable
fun ChangeThemeScreen(
	viewModel: ChangeThemeViewModel = hiltViewModel(),
) {
	val currentThemeMode by viewModel.themeMode.collectAsState()

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp),
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
		Spacer(Modifier.height(16.dp))
		ThemeMode.entries.forEach { themeMode ->
			Row (
				modifier = Modifier
					.clickable {
						viewModel.setThemeMode(themeMode)
					}
					.fillMaxWidth()
					.padding(vertical = 8.dp),
				verticalAlignment = Alignment.CenterVertically,
			){
				RadioButton(
					selected = themeMode == currentThemeMode,
					onClick = null)
				Spacer(Modifier.width(16.dp))
				Text(
					text = themeMode.value,
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
	}
}