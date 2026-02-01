package com.example.deniseshop.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Product
import com.example.deniseshop.core.presentation.components.GridProductItem
import kotlin.collections.forEach

@Composable
fun RecentViewedSection(
	products: List<Product>,
	wishlistItems: List<Long>,
	cartItems:  List<Long>,
	onProductClick: (Long) -> Unit,
	onClickSeeAll: () -> Unit,
	onWishlistToggle: (Long) -> Unit,
	onCartToggle: (Long) -> Unit,
	modifier: Modifier = Modifier,
){
	Column(modifier) {
		Row(
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.recentViewed),
				style = MaterialTheme.typography.titleMedium
			)
			TextButton(
				onClick = { onClickSeeAll() },
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				Text(
					text = stringResource(R.string.seeAll),
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
		Spacer(Modifier.height(16.dp))
		LazyRow {
			item { Spacer(Modifier.width(16.dp)) }
			products.forEach { product ->
				item {
					GridProductItem(
						product = product,
						inWishlist = wishlistItems.contains(product.id),
						inCart = cartItems.contains(product.id),
						modifier = Modifier
							.width(170.dp),
						onClick = onProductClick,
						onCartToggle = onCartToggle,
						onWishlistToggle = onWishlistToggle,
					)
					Spacer(Modifier.width(8.dp))
				}
			}
			item { Spacer(Modifier.width(8.dp)) }
		}
	}
}