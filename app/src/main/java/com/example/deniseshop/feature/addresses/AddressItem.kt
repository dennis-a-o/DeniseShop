package com.example.deniseshop.feature.addresses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Address

@Composable
fun AddressItem(
	address: Address,
	onEditClick: (Long) -> Unit,
	onMakeDefaultClick: (Long) -> Unit,
	onDeleteClick: (Long) -> Unit,
	modifier: Modifier = Modifier,
){
	var checkedState by rememberSaveable { mutableStateOf(false) }

	Column(
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(16.dp)
			.fillMaxWidth()
	){
		Text(
			text = address.name,
			modifier = Modifier.fillMaxWidth(),
			style = MaterialTheme.typography.titleMedium.copy(
				color = MaterialTheme.colorScheme.onBackground,
			),
			maxLines = 1
		)
		Text(
			text = address.email,
			modifier = Modifier.fillMaxWidth(),
			style = MaterialTheme.typography.bodyMedium.copy(
				color = MaterialTheme.colorScheme.onBackground,
			),
			maxLines = 1
		)
		Text(
			text = address.phone,
			modifier = Modifier.fillMaxWidth(),
			style = MaterialTheme.typography.bodyMedium.copy(
				color = MaterialTheme.colorScheme.onBackground,
			),
			maxLines = 1
		)
		Text(
			text = "${address.address}, ${address.state}, ${address.city}, ${address.country}",
			style = MaterialTheme.typography.bodyMedium.copy(
				color = MaterialTheme.colorScheme.outline
			),
		)
		Spacer(Modifier.height(8.dp))
		Row (
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		){
			Row(
				verticalAlignment = Alignment.CenterVertically,
			){
				if (address.default){
					Text(
						text = stringResource(R.string.defaults),
						modifier = Modifier
							.background(
								color = MaterialTheme.colorScheme.inversePrimary,
								shape = RoundedCornerShape(16.dp)
							)
							.padding(horizontal = 8.dp),
						style = MaterialTheme.typography.bodySmall
					)
				} else {
					Checkbox(
						checked = checkedState,
						onCheckedChange = {
							checkedState = it
							onMakeDefaultClick(address.id)
						},
						modifier = Modifier
							.size(16.dp)
					)
					Spacer(Modifier.width(16.dp))
					Text(
						text = stringResource(R.string.setAsDefault),
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
			Row (verticalAlignment = Alignment.CenterVertically){
				IconButton(
					onClick = { onEditClick(address.id) },
					modifier = Modifier.size(20.dp)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_edit_outlined),
						contentDescription = "",
						tint = MaterialTheme.colorScheme.primary
					)
				}
				Spacer(Modifier.width(16.dp))
				IconButton(
					onClick = { onDeleteClick(address.id) },
					modifier = Modifier.size(20.dp)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_delete),
						contentDescription = "",
						tint = MaterialTheme.colorScheme.primary
					)
				}
			}
		}
	}
}