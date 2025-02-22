package com.example.deniseshop.ui.components.bars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R

@Composable
fun ProductConfigBar(
	modifier: Modifier = Modifier,
	onClickLayout: () -> Unit,
	onClickSort: () -> Unit,
	onClickFilter: () -> Unit,
	isGrid: Boolean,
){
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 8.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		TextButton(
			onClick = { onClickSort() },
		) {
			Icon(
				painter = painterResource(id = R.drawable.ic_sort),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(4.dp))
			Text(
				text = stringResource(R.string.sortBy),
				modifier = Modifier.alpha(0.8f),
			)
		}
		Row {
			Row {
				TextButton(onClick = { onClickFilter() }) {
					Icon(
						painter = painterResource(id = R.drawable.ic_filter),
						contentDescription = "",
						tint = MaterialTheme.colorScheme.primary
					)
					Spacer(Modifier.width(4.dp))
					Text(text = stringResource(R.string.filterBy), modifier = Modifier.alpha(0.8f))
				}
				Spacer(Modifier.width(8.dp))
				IconButton(onClick = { onClickLayout() }) {
					Icon(
						painter = painterResource(id = if (isGrid){
							R.drawable.ic_list_view
						}else{
							R.drawable.ic_grid_view
						}),
						contentDescription = "",
						tint = MaterialTheme.colorScheme.primary
					)
				}
			}
		}
	}
}