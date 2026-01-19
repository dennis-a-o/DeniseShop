package com.example.deniseshop.feature.productdetail.components

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Product
import com.example.deniseshop.core.presentation.components.RatingStar
import com.example.deniseshop.feature.productdetail.ProductDetailEvent
import com.example.deniseshop.ui.theme.Orange100
import java.util.Locale

@Composable
fun DetailsSection(
	product: Product,
	selectedQuantity: Int,
	inWishlist: Boolean,
	inCart: Boolean,
	onEvent: (ProductDetailEvent) -> Unit,
	onClickBrand: (Long) -> Unit,
	onClickCategory: (Long) -> Unit,
	modifier: Modifier = Modifier,
){
	Column(modifier = modifier
		.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
		.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
		.padding(16.dp)
		.fillMaxWidth()
	) {
		Text(
			text =product.name,
			style = MaterialTheme.typography.titleMedium,
			maxLines = 3
		)
		Spacer(Modifier.height(8.dp))
		Row (
			modifier = Modifier
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		){
			Row(verticalAlignment = Alignment.CenterVertically) {
				Text(
					text = product.activePrice,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					),
					maxLines = 1,
				)
				Spacer(Modifier.width(8.dp))
				if (product.percentageDiscount < 0) {
					Text(
						text = product.price,
						style = MaterialTheme.typography.bodySmall.copy(
							color = MaterialTheme.colorScheme.outline,
							textDecoration = TextDecoration.LineThrough
						)
					)
					Spacer(Modifier.width(8.dp))
					Text(
						text = "${String.format(Locale.getDefault(),"%.0f", product.percentageDiscount)}%",
						modifier = Modifier
							.background(
								MaterialTheme.colorScheme.inversePrimary,
								RoundedCornerShape(8.dp)
							)
							.padding(horizontal = 8.dp),
						color = MaterialTheme.colorScheme.primary,
						style = MaterialTheme.typography.bodySmall,
						maxLines = 1
					)
				}
			}
			if(!inCart) {
				Row(
					verticalAlignment = Alignment.CenterVertically
				) {
					IconButton(
						onClick = {
							onEvent(ProductDetailEvent.DecreaseQuantity(selectedQuantity))
						},
						modifier = Modifier
							.shadow(2.dp, CircleShape)
							.size(32.dp),
						colors = IconButtonDefaults
							.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_remove),
							contentDescription = null,
							modifier = Modifier.size(12.dp),
							tint = MaterialTheme.colorScheme.primary
						)
					}
					Spacer(Modifier.width(8.dp))
					Text(
						text = selectedQuantity.toString(),
						style = MaterialTheme.typography.bodySmall
					)
					Spacer(Modifier.width(8.dp))
					IconButton(
						onClick = { onEvent(ProductDetailEvent.IncreaseQuantity(selectedQuantity)) },
						modifier = Modifier
							.shadow(2.dp, CircleShape)
							.size(32.dp),
						colors = IconButtonDefaults
							.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_add),
							contentDescription = null,
							modifier = Modifier.size(12.dp),
							tint = MaterialTheme.colorScheme.primary
						)
					}
				}
			}
		}
		Row {
			if (product.quantity == 0){
				Text(
					text = stringResource(R.string.outOfStock),
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.error
					)
				)
			}else if( product.quantity < 5){
				Text(
					text = stringResource(R.string.fewUnitLeft),
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
					)
				)
			}else{
				Text(
					text = stringResource(R.string.inStock),
					style = MaterialTheme.typography.bodyMedium.copy(
						color = Color.Green
					)
				)
			}
		}
		Spacer(Modifier.height(8.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		){
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				RatingStar(
					rating = product.averageRating.toInt(),
					onStartClick = {},
					starModifier = Modifier.size(14.dp),
					startTint = Orange100
				)
				Spacer(Modifier.width(8.dp))
				Text(
					text = "(${product.reviewCount} reviews)",
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
					),
				)
			}
			IconButton(
				onClick = {
					onEvent(ProductDetailEvent.ToggleWishlist)
				},
				modifier = Modifier.size(20.dp)
			) {
				Icon(
					painter = painterResource(id = if(inWishlist){
						R.drawable.ic_favorite_filled
					}else{
						R.drawable.ic_favorite_outline
					}) ,
					contentDescription = "" ,
					tint = MaterialTheme.colorScheme.primary
				)
			}
		}
		product.brand?.let { brand ->
			Spacer(Modifier.height(8.dp))
			Row {
				Text(
					text = stringResource(R.string.brand_c),
					style = MaterialTheme.typography.bodyMedium
				)
				Spacer(Modifier.width(8.dp))
				Text(
					text = brand.name,
					modifier = Modifier.clickable {
						onClickBrand(brand.id)
					},
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
					)
				)
			}
		}
		product.categories?.let { categories ->
			Spacer(Modifier.height(8.dp))
			Row {
				Text(
					text = stringResource(R.string.category_c),
					style = MaterialTheme.typography.bodyMedium
				)
				Spacer(Modifier.width(8.dp))
				categories.forEach {
					Text(
						text = it.name,
						modifier = Modifier.clickable {
							onClickCategory(it.id)
						},
						style = MaterialTheme.typography.bodyMedium.copy(
							color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
						)
					)
					Spacer(Modifier.width(4.dp))
				}
			}
		}
	}
}