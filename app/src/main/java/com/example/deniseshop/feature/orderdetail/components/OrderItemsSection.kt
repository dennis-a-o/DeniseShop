package com.example.deniseshop.feature.orderdetail.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.OrderProduct

@Composable
fun OrderItemsSection(
	products: List<OrderProduct>,
	isDownloading: Boolean,
	onReviewClick: (Long) -> Unit,
	onDownloadClick: (Long) -> Unit,
	modifier: Modifier = Modifier,
){
	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(16.dp)
			.fillMaxWidth()
	){
		Text(
			text = stringResource(R.string.products),
			style = MaterialTheme.typography.titleMedium
		)
		Spacer(Modifier.height(8.dp))
		products.forEachIndexed{ index,product ->
			OrderItem(
				product = product,
				isDownloading = isDownloading,
				onReviewClick = onReviewClick,
				onDownloadClick = onDownloadClick,
			)
			if(index != (products.size - 1)){
				HorizontalDivider(thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
			}
		}
	}
}

@Composable
private fun OrderItem(
	product: OrderProduct,
	isDownloading: Boolean,
	onReviewClick: (Long) -> Unit,
	onDownloadClick: (Long) -> Unit,
	modifier: Modifier = Modifier,
){
	Column (modifier = modifier){
		Row (verticalAlignment = Alignment.Top) {
			AsyncImage(
				model = product.image,
				contentDescription = "",
				modifier = Modifier
					.width(80.dp)
					.height(80.dp)
					.clip(RoundedCornerShape(14.dp)),
				contentScale = ContentScale.Crop
			)
			Spacer(Modifier.width(8.dp))
			Column {
				Text(
					text = product.name,
					style = MaterialTheme.typography.bodyLarge,
					maxLines = 2
				)
				Text(
					text = "Qty: ${product.quantity}",
					style = MaterialTheme.typography.bodyMedium
				)
				Text(
					text = product.price,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontWeight = FontWeight.W500
					),
					maxLines = 1
				)
			}
		}
		Spacer(Modifier.height(8.dp))
		Row (horizontalArrangement = Arrangement.SpaceBetween){
			if(!(product.rated)) {
				OutlinedButton(
					onClick = {
						onReviewClick(product.id)
					},
					modifier = Modifier
						.weight(50f),
					shape = RoundedCornerShape(16.dp),
					border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
				) {
					Row(verticalAlignment = Alignment.CenterVertically) {
						Icon(
							painter = painterResource(R.drawable.ic_rate_review_outlined),
							contentDescription = "",
							modifier = Modifier.size(20.dp)
						)
						Spacer(Modifier.width(4.dp))
						Text(text = stringResource(R.string.write_review))
					}
				}
				Spacer(Modifier.width(8.dp))
			}
			if (product.downloadable) {
				Button(
					onClick = {
						if (!isDownloading) {
							onDownloadClick(product.id)
						}
					},
					modifier = Modifier
						.weight(50f),
					shape = RoundedCornerShape(16.dp)
				) {
					if (isDownloading){
						CircularProgressIndicator(
							modifier = Modifier.size(14.dp),
							trackColor = MaterialTheme.colorScheme.surfaceContainerLowest
						)
					}else {
						Text(text = stringResource(R.string.download))
					}
				}
			}
		}
	}
}