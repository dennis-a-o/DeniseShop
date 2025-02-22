package com.example.deniseshop.ui.components.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R

@Composable
fun ProductSortFilter(
	onSelectOption: (SortOption) -> Unit,
	onDismiss: () -> Unit,
	selectedOption: SortOption
) {
	Column(
		Modifier
			.fillMaxWidth()
			.heightIn(max = 300.dp)
			.padding(horizontal = 8.dp),
	) {
		Text(
			text = stringResource(R.string.sortBy),
			modifier = Modifier.fillMaxWidth(),
			style = MaterialTheme.typography.titleLarge,
			textAlign = TextAlign.Center
		)
		Spacer(Modifier.height(8.dp))
		SortOption.entries.forEach { option ->
			Row (
				modifier = Modifier
					.clickable {
						onSelectOption(option)
						onDismiss()
					}
					.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically
			){
				RadioButton(selected =  selectedOption == option, onClick = null )
				Spacer(Modifier.width(8.dp))
				Text(
					text = option.displayName,
					style = MaterialTheme.typography.bodyLarge
				)
			}
			Spacer(Modifier.height(8.dp))
		}
	}
}

enum class SortOption(val displayName: String){
	RATING_ASCENDING("Rating - Low to High"),
	RATING_DESCENDING("Rating - High to Low"),
	DATE_ASCENDING("Newest First"),
	DATE_DESCENDING("Oldest First"),
	PRICE_ASCENDING("Price - Low to High"),
	PRICE_DESCENDING("Price - High to Low"),
	NAME_ASCENDING("Name - A to Z"),
	NAME_DESCENDING("Name - Z to A")
}