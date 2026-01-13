package com.example.deniseshop.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.ProductSortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSortOptionBottomSheet(
	sortOption: ProductSortOption,
	onSortOptionSelect:(ProductSortOption) -> Unit,
	onDismiss: () -> Unit,
) {
	ModalBottomSheet(
		onDismissRequest = onDismiss,
		sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
	) {
		Column(
			Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp),
		) {
			Text(
				text = stringResource(R.string.sortBy),
				modifier = Modifier.fillMaxWidth(),
				style = MaterialTheme.typography.titleLarge,
				textAlign = TextAlign.Center
			)
			Spacer(Modifier.height(8.dp))
			ProductSortOption.entries.forEach { option ->
				Row (
					modifier = Modifier
						.clickable {
							onSortOptionSelect(option)
							onDismiss()
						}
						.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				){
					RadioButton(
						selected =  sortOption == option,
						onClick = null
					)
					Spacer(Modifier.width(8.dp))
					Text(
						text = option.displayName,
						style = MaterialTheme.typography.bodyLarge
					)
				}
				Spacer(Modifier.height(16.dp))
			}
		}
	}
}