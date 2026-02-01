package com.example.deniseshop.feature.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Category

@Composable
fun CategorySection(
	categories: List<Category>,
	onClick: (Long) -> Unit,
	modifier: Modifier = Modifier,
){
	Column (modifier){
		Row (modifier = Modifier.padding(horizontal = 16.dp)){
			Text(
				text = stringResource(R.string.categories),
				style = MaterialTheme.typography.titleMedium,
				maxLines = 1
			)
		}
		Spacer(Modifier.height(16.dp))
		Row(
			modifier = Modifier
				.horizontalScroll(
					rememberScrollState()
				)
		) {
			Spacer(Modifier.width(16.dp))
			Button(
				onClick = {},
				modifier = Modifier
			) {
				Text(
					text = stringResource(R.string.all),
					style = MaterialTheme.typography.bodyMedium
				)
			}
			Spacer(modifier = Modifier.width(4.dp))
			categories.forEach {
				OutlinedButton(
					onClick = { onClick(it.id) },
					border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
				) {
					Text(
						text = it.name,
						style = MaterialTheme.typography.bodyMedium
					)
				}
				Spacer(modifier = Modifier.width(4.dp))
			}
			Spacer(Modifier.width(12.dp))
		}
	}
}