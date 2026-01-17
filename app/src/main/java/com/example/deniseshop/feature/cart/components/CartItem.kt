package com.example.deniseshop.feature.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.CartProduct

@Composable
fun CartItem(
	cartItem: CartProduct,
	onClearClick: (Long) -> Unit,
	onIncreaseClick: (Long) -> Unit,
	onDecreaseClick: (Long) -> Unit,
	onClick: (Long) -> Unit,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier
			.shadow(
				elevation = 1.dp,
				shape = RoundedCornerShape(16.dp)
			)
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				onClick(cartItem.productId)
			}
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceEvenly,
		verticalAlignment = Alignment.Top
	) {
		AsyncImage(
			model = cartItem.image,
			modifier = Modifier
				.width(100.dp)
				.height(120.dp)
				.clip(RoundedCornerShape(16.dp)),
			contentDescription = "",
			contentScale = ContentScale.Crop
		)
		Column(
			modifier = Modifier
				.padding(8.dp)
		) {
			Text(
				text = cartItem.name,
				style = MaterialTheme.typography.bodyLarge,
				maxLines = 2,
			)
			Row (
				verticalAlignment = Alignment.CenterVertically
			){
				Text(
					text = cartItem.activePrice,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground,
						fontWeight = FontWeight.W500
					)
				)
				Spacer(Modifier.width(4.dp))
				if(cartItem.percentageDiscount < 0) {
					Text(
						text = cartItem.price,
						style = MaterialTheme.typography.bodySmall.copy(
							color = MaterialTheme.colorScheme.outline,
							textDecoration = TextDecoration.LineThrough
						)
					)
				}
			}
			cartItem.color?.let {
				Row {
					Text(
						text = stringResource(R.string.color_),
						style = MaterialTheme.typography.bodyMedium.copy(
							color = MaterialTheme.colorScheme.outline
						)
					)
					Spacer(Modifier.width(4.dp))
					Text(
						text = cartItem.color,
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
			cartItem.size?.let {
				Row {
					Text(
						text = stringResource(R.string.size_),
						style = MaterialTheme.typography.bodyMedium.copy(
							color = MaterialTheme.colorScheme.outline
						)
					)
					Spacer(Modifier.width(4.dp))
					Text(
						text = cartItem.size,
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
			Spacer(Modifier.height(4.dp))
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.Top
			) {
				Row(
					horizontalArrangement = Arrangement.Start,
					verticalAlignment = Alignment.CenterVertically
				) {
					IconButton(
						onClick = { onDecreaseClick(cartItem.id) },
						modifier = Modifier
							.shadow(1.dp, CircleShape)
							.size(32.dp),
						colors = IconButtonDefaults
							.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
					) {
						Icon(
							painter = painterResource(R.drawable.ic_remove),
							contentDescription = "",
							modifier = Modifier.size(14.dp),
							tint = MaterialTheme.colorScheme.primary
						)
					}
					Spacer(Modifier.width(8.dp))
					Text(
						text = cartItem.quantity.toString(),
						style = MaterialTheme.typography.bodyMedium
					)
					Spacer(Modifier.width(8.dp))
					IconButton(
						onClick = { onIncreaseClick(cartItem.id) },
						modifier = Modifier
							.shadow(1.dp, CircleShape)
							.size(32.dp),
						colors = IconButtonDefaults
							.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
					) {
						Icon(
							painter = painterResource(R.drawable.ic_add),
							contentDescription = "",
							modifier = Modifier.size(14.dp),
							tint = MaterialTheme.colorScheme.primary
						)
					}
				}
				IconButton(
					onClick = {  onClearClick(cartItem.productId) },
					modifier = Modifier.size(32.dp)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_close),
						contentDescription = "",
						modifier = Modifier.scale(0.8f)
					)
				}
			}
		}
	}
}