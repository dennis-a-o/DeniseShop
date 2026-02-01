package com.example.deniseshop.feature.orderdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.OrderAddress

@Composable
fun CustomerAddressSection(
	address: OrderAddress,
	modifier: Modifier = Modifier,
){
	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(16.dp)
			.fillMaxWidth()
	) {
		Text(
			text = stringResource(R.string.customer),
			style = MaterialTheme.typography.titleMedium
		)
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = stringResource(R.string.name_),
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = address.name,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = stringResource(R.string.phone_),
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = address.phone,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = stringResource(R.string.email_),
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = address.email,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Row {
			Text(
				text = stringResource(R.string.address_),
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = address.address,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
	}
}