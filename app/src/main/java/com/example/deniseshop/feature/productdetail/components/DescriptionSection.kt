package com.example.deniseshop.feature.productdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.presentation.components.HtmlText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionSection(
	summary: String?,
	description: String?,
	modifier: Modifier = Modifier,
) {

	var showFullDescription by remember { mutableStateOf(false) }

	if (showFullDescription) {
		ModalBottomSheet(
			onDismissRequest = {
				showFullDescription = false
			},
			sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
			containerColor = MaterialTheme.colorScheme.background
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically
			){
				IconButton(
					onClick = { showFullDescription = false }
				) {
					Icon(
						painter = painterResource(R.drawable.ic_arrow_back),
						contentDescription = ""
					)
				}
				Spacer(Modifier.width(16.dp))
				Text(
					text = stringResource(R.string.productDescription),
					style = MaterialTheme.typography.titleLarge
				)
			}
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(16.dp)
			) {
				description?.let {
					HtmlText(
						html = it,
						modifier = Modifier
							.verticalScroll(rememberScrollState())
					)
				}
			}
		}
	}


	Column(
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.fillMaxWidth()
			.padding(16.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.descriptions),
				style = MaterialTheme.typography.titleMedium
			)
			description?.let {
				TextButton(
					onClick = {
						showFullDescription = true
					},
					modifier = Modifier
						.padding(0.dp),
				) {
					Text(
						text = stringResource(R.string.seeAll),
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
		}
		summary?.let {
			Text(
				text = it,
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.outline
				)
			)
		}
	}
}
