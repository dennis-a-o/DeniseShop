package com.example.deniseshop.feature.checkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.deniseshop.core.domain.model.Checkout

@Composable
fun CheckoutSummarySection(
	checkout: Checkout,
	modifier: Modifier = Modifier,
){
	Column(
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(16.dp)
	) {
		Column{
			Text(
				text = "Order Summary (${checkout.items.size} items)",
				style = MaterialTheme.typography.titleMedium
			)
		}
		Spacer(Modifier.height(8.dp))
		Column {
			Row (
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			){
				Text(
					text = stringResource(R.string.subTotal),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.outline
				)
				Text(
					text = checkout.subTotal,
					style = MaterialTheme.typography.bodyMedium
				)
			}
			if (checkout.couponDiscount != null) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = stringResource(R.string.couponCodeDiscount),
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.outline
					)
					Text(
						text = checkout.couponDiscount,
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
			if (checkout.shippingFee != null) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = stringResource(R.string.shippingFee),
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.outline
					)
					Text(
						text = checkout.shippingFee,
						style = MaterialTheme.typography.bodyMedium,
					)
				}
			}
			Row (
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			){
				Text(
					text = stringResource(R.string.totalAmount),
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					)
				)
				Text(
					text = checkout.totalAmount,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					)
				)
			}
		}
	}
}