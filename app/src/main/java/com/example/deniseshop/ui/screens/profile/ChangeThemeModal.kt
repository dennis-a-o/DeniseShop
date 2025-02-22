package com.example.deniseshop.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.ui.models.ThemeConfig
import com.example.deniseshop.ui.models.UiSetting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun ChangeThemeModal(
	modifier: Modifier = Modifier,
	onClose: () -> Unit,
	onSelect: (ThemeConfig) -> Unit,
	uiSetting: UiSetting
){
	val skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
	val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

	ModalBottomSheet(
		onDismissRequest = { onClose() },
		sheetState = bottomSheetState,
	) {
		Column(
			modifier = modifier
				.fillMaxWidth()
				.heightIn(max = 300.dp)
				.padding(horizontal = 8.dp),
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center
			) {
				Text(
					text = "Choose Theme",
					style = MaterialTheme.typography.titleLarge
				)
			}
			Spacer(Modifier.height(8.dp))
			ThemeItem(
				text = stringResource(R.string.systemDefault) ,
				selected = uiSetting.theme == ThemeConfig.FOLLOW_SYSTEM,
				onSelect = {  onSelect(ThemeConfig.FOLLOW_SYSTEM) }
			)
			ThemeItem(
				text = stringResource(R.string.light) ,
				selected = uiSetting.theme == ThemeConfig.LIGHT,
				onSelect = {  onSelect(ThemeConfig.LIGHT) }
			)
			ThemeItem(
				text = stringResource(R.string.dark) ,
				selected = uiSetting.theme == ThemeConfig.DARK,
				onSelect = {  onSelect(ThemeConfig.DARK) }
			)
		}
	}
}

@Composable
private fun ThemeItem(
	text: String,
	selected: Boolean,
	onSelect: () -> Unit
){
	Row (
		modifier = Modifier
			.clickable { onSelect() }
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically
	){
		RadioButton(selected = selected, onClick = {})
		Text(
			text = text,
			style = MaterialTheme.typography.bodyMedium
		)
	}
}