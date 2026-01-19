package com.example.deniseshop.feature.productdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.ProductDetail
import com.example.deniseshop.core.presentation.components.ReviewItem
import com.example.deniseshop.core.presentation.components.ReviewStatItem

@Composable
fun ReviewSection(
	productDetail: ProductDetail,
	onSeeAllReview: (Long) -> Unit,
	modifier: Modifier = Modifier,
){
	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(16.dp)
	){
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.reviews),
				style = MaterialTheme.typography.titleMedium
			)
			if(productDetail.reviews.size > productDetail.product.reviewCount) {
				TextButton(onClick = {
					onSeeAllReview(productDetail.product.id)
				}
				) {
					Text(
						text = stringResource(R.string.seeAll),
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
		}
		ReviewStatItem(reviewStat = productDetail.reviewStat)
		productDetail.reviews.forEach {
			Spacer(Modifier.height(16.dp))
			HorizontalDivider(thickness = 0.5.dp)
			Spacer(Modifier.height(16.dp))
			ReviewItem(
				review = it
			)
		}
	}
}