package com.example.deniseshop.feature.checkout.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.CartProduct

@Composable
fun CheckoutItem(
	cartProduct: CartProduct,
	modifier: Modifier = Modifier,
){
	Column(
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(16.dp)
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceEvenly,
			verticalAlignment = Alignment.Top
		) {
			Image(
				painter = rememberAsyncImagePainter(model = cartProduct.image),
				modifier = Modifier
					.width(100.dp)
					.height(120.dp)
					.clip(RoundedCornerShape(14.dp)),
				contentDescription = "",
				contentScale = ContentScale.Crop
			)
			Column(
				modifier = Modifier
					.padding(start = 8.dp)
			) {
				Text(
					text = cartProduct.name,
					style = MaterialTheme.typography.bodyLarge,
					maxLines = 2,
				)
				Row (verticalAlignment = Alignment.CenterVertically){
					Text(
						text = cartProduct.activePrice,
						style = MaterialTheme.typography.bodyMedium.copy(
							fontWeight = FontWeight.W500
						)
					)
					Spacer(Modifier.width(4.dp))
					if (cartProduct.percentageDiscount < 0) {
						Text(
							text = cartProduct.price,
							style = MaterialTheme.typography.bodySmall.copy(
								textDecoration = TextDecoration.LineThrough,
								color = MaterialTheme.colorScheme.outline
							)
						)
					}
				}
				if (cartProduct.color !=null) {
					Row {
						Text(
							text = stringResource(R.string.color_),
							style = MaterialTheme.typography.bodyMedium.copy(
								color = MaterialTheme.colorScheme.outline
							)
						)
						Spacer(Modifier.width(4.dp))
						Text(
							text = cartProduct.color,
							style = MaterialTheme.typography.bodyMedium
						)
					}
				}
				if (cartProduct.size != null) {
					Row {
						Text(
							text = stringResource(R.string.size_),
							style = MaterialTheme.typography.bodyMedium.copy(
								color = MaterialTheme.colorScheme.outline
							)
						)
						Spacer(Modifier.width(4.dp))
						Text(
							text = cartProduct.size,
							style = MaterialTheme.typography.bodyMedium
						)
					}
				}
				Row {
					Text(
						text = stringResource(R.string.qty_),
						style = MaterialTheme.typography.bodyMedium.copy(
							color = MaterialTheme.colorScheme.outline
						)
					)
					Spacer(Modifier.width(4.dp))
					Text(
						text = cartProduct.quantity.toString(),
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
		}
		HorizontalDivider(
			thickness = 0.5.dp,
			modifier = Modifier
				.padding(vertical = 8.dp)
		)
		Column {
			Row (
				modifier = Modifier
					.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			){
				Text(
					text = stringResource(R.string.netAmount),
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.outline
					)
				)
				Text(
					text = "${cartProduct.activePrice} x ${cartProduct.quantity}",
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.outline
					)
				)
				Text(
					text = cartProduct.totalPrice,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.outline
					)
				)
			}
			Row (
				modifier = Modifier
					.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			){
				Text(
					text = stringResource(R.string.total),
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.outline
					)
				)
				Text(
					text = cartProduct.totalPrice,
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
	}
}