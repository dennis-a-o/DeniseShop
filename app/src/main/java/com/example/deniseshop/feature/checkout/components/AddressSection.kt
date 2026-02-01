package com.example.deniseshop.feature.checkout.components

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Address

@Composable
fun AddressSection(
	address: Address?,
	onEditClick: (Long) -> Unit,
	onAddClick: () -> Unit,
	modifier: Modifier = Modifier,
){
	Column (
		modifier = modifier
			.fillMaxWidth()
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(16.dp)
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_location_on),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary,
				modifier = Modifier
					.size(20.dp)
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = stringResource(R.string.address),
				style = MaterialTheme.typography.titleMedium
			)
		}
		Spacer(Modifier.height(8.dp))
		if(address != null){
			Column (Modifier.padding(8.dp)){
				Row (
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween
				){
					Text(
						text = address.name,
						style = MaterialTheme.typography.bodyMedium.copy(
							fontWeight = FontWeight.W500
						)
					)
					IconButton(
						onClick = {
							onEditClick(address.id)
						},
						modifier = Modifier
							.size(20.dp)
					) {
						Icon(
							painter = painterResource(R.drawable.ic_edit_outlined),
							contentDescription = null
						)
					}
				}
				Text(
					text = "${address.email}\n${address.phone}\n${address.address},${address.state},${address.city},${address.country}",
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.outline
					)
				)
			}
		}else {
			Column(
				modifier = Modifier
					.padding(8.dp)
			) {
				OutlinedButton(
					onClick = onAddClick,
					shape = RoundedCornerShape(16.dp)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_add),
						contentDescription = "",
						modifier = Modifier
							.size(14.dp)
					)
					Spacer(modifier.width(8.dp))
					Text(text = "Add")
				}
			}
		}
	}
}