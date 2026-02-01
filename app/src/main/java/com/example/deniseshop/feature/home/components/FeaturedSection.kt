package com.example.deniseshop.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.deniseshop.core.presentation.components.LazyRowGrid
import com.example.deniseshop.core.presentation.components.ListProductItem

@Composable
fun FeaturedSection(
	products: List<Product>,
	wishlistItems: List<Long>,
	cartItems:  List<Long>,
	onClickSeeAll: () -> Unit,
	onProductClick: (Long) -> Unit,
	onWishlistToggle: (Long) -> Unit,
	onCartToggle: (Long) -> Unit,
	modifier: Modifier = Modifier,
){
	Column (modifier = modifier){
		Row(
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.featuredProducts),
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
		LazyRowGrid(
			rows = 2,
			itemCount = products.size,
			gridWidth = 320.dp,
			gridWidthSpacing = 8.dp,
			gridHeightSpacing = 8.dp,
			gridHorizontalPadding = 16.dp
		){ index ->
			ListProductItem(
				product = products[index],
				inWishlist = wishlistItems.contains(products[index].id),
				inCart = cartItems.contains(products[index].id),
				onClick = onProductClick,
				onWishlistToggle = onWishlistToggle,
				onCartToggle = onCartToggle
			)
		}
	}
}