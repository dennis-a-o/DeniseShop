package com.example.deniseshop.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.ui.theme.Orange100
import com.example.deniseshop.R
import com.example.deniseshop.ui.models.UiProduct
import java.util.Locale

@Composable
fun GridProductItem(
	modifier: Modifier = Modifier,
	product: UiProduct ,
	onClick: (Long) -> Unit,
	onToggleCart: (Long) -> Unit = {},
	onToggleWishlist: (Long) -> Unit = {},
	wishlistItems: Set<Long> = emptySet(),
	cartItems: Set<Long> = emptySet()
){
	Column(
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable { onClick(product.id) }
			.wrapContentWidth()
	){
		Box {
			if (product.percentageDiscount < 0f) {
				Text(
					text = String.format(Locale.getDefault(),"%.0f", product.percentageDiscount)+"%",
					style = MaterialTheme.typography.bodySmall.copy(
						color = Color.White
					),
					modifier = Modifier
						.padding(start = 8.dp, top = 8.dp)
						.background(color = Color.Red, shape = RoundedCornerShape(8.dp))
						.zIndex(1f)
						.padding(horizontal = 4.dp)
				)
			}
			Image(
				painter = rememberAsyncImagePainter(model = product.image) ,
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = Modifier
					.height(140.dp)
					.fillMaxWidth()
					.clip(RoundedCornerShape(14.dp))
			)
			Row (
				modifier = Modifier
					.align(Alignment.BottomEnd)
					.padding(bottom = 4.dp, end = 4.dp)
			){
				IconButton(
					onClick = { onToggleCart(product.id) },
					modifier = Modifier
						.shadow(1.dp, CircleShape)
						.size(32.dp),
					colors = IconButtonDefaults
						.iconButtonColors( containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
				) {
					Icon(
						painter = painterResource(
							id = if(cartItems.contains(product.id)){
								R.drawable.ic_cart_filled
							}else{
								R.drawable.ic_cart_outline
							},
						),
						contentDescription = null,
						modifier = Modifier.size(14.dp),
						tint = MaterialTheme.colorScheme.primary
					)
				}
				Spacer(Modifier.width(4.dp))
				IconButton(
					onClick = { onToggleWishlist(product.id) },
					modifier = Modifier
						.shadow(1.dp, CircleShape)
						.size(32.dp),
					colors = IconButtonDefaults
						.iconButtonColors( containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
				) {
					Icon(
						painter = painterResource(
							id = if(wishlistItems.contains(product.id)){
								R.drawable.ic_favorite_filled
							}else{
								R.drawable.ic_favorite_outline
							},
						),
						contentDescription = null,
						modifier = Modifier.size(14.dp),
						tint = MaterialTheme.colorScheme.primary
					)
				}
			}
		}
		Column(
			Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
		) {
			Text(
				text = product.name,
				style = MaterialTheme.typography.bodyLarge,
				maxLines = 2
			)
			Spacer(modifier = Modifier.height(4.dp))
			Row (verticalAlignment = Alignment.CenterVertically){
				Text(
					text = product.activePrice,
					style = MaterialTheme.typography.bodyLarge.copy(
						fontWeight = FontWeight.W500
					)
				)
				Spacer(Modifier.width(4.dp))
				if(product.percentageDiscount < 0f) {
					Text(
						text = product.price,
						style = MaterialTheme.typography.bodySmall.copy(
							color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
							textDecoration = TextDecoration.LineThrough
						),
						maxLines = 1
					)
				}
			}
			Spacer(modifier = Modifier.height(4.dp))
			Row (Modifier.padding(start = 4.dp, bottom = 4.dp)){
				RatingStar(
					rating = product.averageRating.toInt(),
					onStartClick = {},
					starModifier = Modifier.size(14.dp),
					startTint = Orange100
				)
				Text(
					text = "(${product.reviewCount})",
					style = MaterialTheme.typography.bodySmall.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
					)
				)
			}
		}
	}
}

@Composable
fun ListProductItem(
	modifier: Modifier = Modifier,
	product: UiProduct,
	onClick: (Long) -> Unit,
	onToggleCart: (Long) -> Unit ,
	onToggleWishlist: (Long) -> Unit,
	wishlistItems: Set<Long> = emptySet(),
	cartItems: Set<Long> = emptySet()
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable { onClick(product.id) }
			.fillMaxWidth()
	){
		Box{
			if (product.percentageDiscount < 0f) {
				Text(
					text = String.format(Locale.getDefault(),"%.0f", product.percentageDiscount)+"%",
					style = MaterialTheme.typography.bodySmall.copy(
						color = Color.White
					),
					modifier = Modifier
						.padding(start = 8.dp, top = 8.dp)
						.background(color = Color.Red, shape = RoundedCornerShape(8.dp))
						.zIndex(1f)
						.padding(horizontal = 4.dp)
				)
			}
			Image(
				painter = rememberAsyncImagePainter(model = product.image),
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = Modifier
					.height(122.dp)
					.width(100.dp)
					.clip(RoundedCornerShape(14.dp))
			)
		}
		Column (Modifier.padding(horizontal = 6.dp, vertical = 4.dp)){
			Row {
				Text(
					text = product.name,
					style = MaterialTheme.typography.bodyLarge,
					maxLines = 2
				)
			}
			Spacer(modifier = Modifier.height(4.dp))
			Row (verticalAlignment = Alignment.CenterVertically){
				Text(
					text = product.activePrice,
					style = MaterialTheme.typography.bodyLarge.copy(
						fontWeight = FontWeight.W500
					)
				)
				Spacer(Modifier.width(4.dp))
				if (product.percentageDiscount < 0f) {
					Text(
						text = product.price,
						style = MaterialTheme.typography.bodySmall.copy(
							color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
							textDecoration = TextDecoration.LineThrough
						),
					)
				}
			}
			Spacer(modifier = Modifier.height(4.dp))
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Row {
					RatingStar(
						rating = product.averageRating.toInt(),
						onStartClick = {},
						starModifier = Modifier.size(14.dp),
						startTint = Orange100
					)
					Text(
						text = "(${product.reviewCount})",
						style = MaterialTheme.typography.bodySmall.copy(
							color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
						)
					)
				}
				Row (
					modifier = Modifier
						.padding(end = 4.dp, bottom = 2.dp)
				){
					IconButton(
						onClick = { onToggleCart(product.id) },
						modifier = Modifier
							.shadow(1.dp, CircleShape)
							.size(32.dp),
						colors = IconButtonDefaults
							.iconButtonColors( containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
					) {
						Icon(
							painter = painterResource(
								id = if(cartItems.contains(product.id)){
									R.drawable.ic_cart_filled
								}else{
									R.drawable.ic_cart_outline
								},
							),
							contentDescription = null,
							modifier = Modifier.size(14.dp),
							tint = MaterialTheme.colorScheme.primary
						)
					}
					Spacer(Modifier.width(4.dp))
					IconButton(
						onClick = { onToggleWishlist(product.id) },
						modifier = Modifier
							.shadow(1.dp, CircleShape)
							.size(32.dp),
						colors = IconButtonDefaults
							.iconButtonColors( containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
					) {
						Icon(
							painter = painterResource(
								id = if(wishlistItems.contains(product.id)){
									R.drawable.ic_favorite_filled
								}else{
									R.drawable.ic_favorite_outline
								},
							),
							contentDescription = null,
							modifier = Modifier.size(14.dp),
							tint = MaterialTheme.colorScheme.primary
						)
					}
				}
			}
		}
	}

}
