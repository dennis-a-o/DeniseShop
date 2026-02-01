package com.example.deniseshop.feature.productdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Product
import com.example.deniseshop.feature.productdetail.ProductDetailEvent
import androidx.core.graphics.toColorInt


@Composable
fun AttributesSection(
	product: Product,
	selectedColor: String?,
	selectedSize: String?,
	onEvent: (ProductDetailEvent) -> Unit,
	modifier: Modifier = Modifier,
){
	Column(
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.fillMaxWidth()
			.padding(16.dp)
	) {
		product.color?.let {
			Row {
				Text(
					text = stringResource(R.string.color_),
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					)
				)
				Spacer(Modifier.width(8.dp))
				Text(
					text = selectedColor ?: product.color.firstOrNull()?.split("=")[0] ?: "",
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
					)
				)
			}
			Spacer(Modifier.height(8.dp))
			LazyRow {
				product.color.forEach {
					val color = it.split("=")
					item {
						IconButton(
							onClick = {  onEvent(ProductDetailEvent.SelectColor(color[0])) },
							modifier = Modifier
								.shadow(1.dp, CircleShape)
								.size(28.dp),
							colors = IconButtonDefaults
								.iconButtonColors(containerColor = Color(color[1].toColorInt()))
						) {
							if (selectedColor == color[0])
								Icon(
									painter = painterResource(id = R.drawable.ic_done),
									contentDescription = "",
									modifier = Modifier.scale(0.8f),
									tint = MaterialTheme.colorScheme.surfaceContainerLow
								)
						}
						Spacer(Modifier.width(8.dp))
					}
				}
			}
		}
		product.size?.let {
			Spacer(Modifier.height(16.dp))
			Row {
				Text(
					text = stringResource(R.string.size_),
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					)
				)
				Spacer(Modifier.width(8.dp))
				Text(
					text = selectedSize ?: product.size.firstOrNull() ?: "",
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
					)
				)
			}
			Spacer(Modifier.height(8.dp))
			LazyRow {
				product.size.forEach { size ->
					item {
						ElevatedFilterChip(
							selected = selectedSize == size,
							onClick = { onEvent(ProductDetailEvent.SelectSize(size)) },
							label = { Text(text = size ) },
							modifier = Modifier.padding(end = 8.dp),
							colors = FilterChipDefaults
								.filterChipColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
						)
					}
				}
			}
		}
	}
}