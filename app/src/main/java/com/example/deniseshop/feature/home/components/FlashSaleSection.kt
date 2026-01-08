package com.example.deniseshop.feature.home.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.FeaturedFlashSale
import com.example.deniseshop.core.presentation.components.CountDownType
import com.example.deniseshop.core.presentation.components.FlashSaleCountDown
import com.example.deniseshop.core.presentation.components.GridProductItem

@Composable
fun FlashSaleSection(
	featuredFlashSale: FeaturedFlashSale,
	cartItems: List<Long> = emptyList(),
	wishlistItems: List<Long> = emptyList(),
	onProductClick: (Long) -> Unit,
	onSeeAllClick: (Long) -> Unit,
	onWishlistToggle: (Long) -> Unit,
	onCartToggle: (Long) -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(modifier) {
		Row(
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.flashSales),
				style = MaterialTheme.typography.titleMedium
			)
			TextButton(
				onClick = { onSeeAllClick(featuredFlashSale.flashSale.id) },
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				Text(
					text = stringResource(R.string.seeAll),
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
		Spacer(Modifier.height(16.dp))
		Row (
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(horizontal = 16.dp)
		){
			Text(
				text = stringResource(R.string.timeLeft),
				style = MaterialTheme.typography.bodySmall.copy(
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
				)
			)
			Spacer(Modifier.width(4.dp))
			FlashSaleCountDown(
				endDate = featuredFlashSale.flashSale.endDate,
				countDownType = CountDownType.DD_HH_MM_SS
			)
		}
		Spacer(Modifier.height(16.dp))
		Row (
			modifier = Modifier
				.horizontalScroll(rememberScrollState())
		){
			Spacer(Modifier.width(16.dp))
			featuredFlashSale.products.forEach { product ->
				GridProductItem(
					product = product,
					modifier = Modifier
						.width(170.dp),
					inWishlist = wishlistItems.contains(product.id),
					inCart = cartItems.contains(product.id),
					onClick = onProductClick,
					onWishlistToggle = onWishlistToggle,
					onCartToggle = onCartToggle,
				)
				Spacer(Modifier.width(8.dp))
			}
			Spacer(Modifier.width(8.dp))
		}
	}
}