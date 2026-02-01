package com.example.deniseshop.feature.cart.components

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Cart

@Composable
fun CartSummary(
	cart: Cart,
	onClearCoupon: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
			.fillMaxWidth()
	) {
		cart.coupon?.let {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Row(
					modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
				) {
					Text(
						text = stringResource(R.string.coupon_discount),
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.outline
					)
					Spacer(Modifier.width(16.dp))
					IconButton(
						onClick = onClearCoupon,
						modifier = Modifier
							.size(16.dp)
					) {
						Icon(
							painter = painterResource(R.drawable.ic_delete),
							contentDescription = null
						)
					}
				}
				Text(
					text = if (cart.couponType == "free_shipping")
						stringResource(R.string.free_shipping)
					else cart.couponDiscount,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.outline
					)
				)
			}
		}
		Spacer(Modifier.height(8.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.totalPrice),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
			Text(
				text = cart.totalPrice,
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground,
					fontWeight = FontWeight.W500
				)
			)
		}
	}
}