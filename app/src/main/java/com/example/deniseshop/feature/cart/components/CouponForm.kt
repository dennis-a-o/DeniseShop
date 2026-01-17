package com.example.deniseshop.feature.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R

@Composable
fun CouponForm(
	isCouponLoading: Boolean,
	onSubmitCoupon: (String) -> Unit,
){
	var couponValue by remember { mutableStateOf("") }

	Row (
		modifier = Modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		TextField(
			value = couponValue,
			onValueChange = { couponValue = it },
			modifier = Modifier
				.weight(70f),
			placeholder = {
				Text(
					text = stringResource(R.string.haveACouponCode),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.outline
				)
			},
			singleLine = true,
			colors = TextFieldDefaults.colors(
				unfocusedIndicatorColor = Color.Transparent,
				focusedIndicatorColor = Color.Transparent,
				unfocusedContainerColor = Color.Transparent,
				focusedContainerColor = Color.Transparent,
			)
		)

		Button(
			onClick = {
				if (couponValue.isNotEmpty() && !isCouponLoading) {
					onSubmitCoupon(couponValue)
				}
			},
			modifier = Modifier
				.weight(30f),
		) {
			if (isCouponLoading){
				CircularProgressIndicator(
					modifier = Modifier
						.size(20.dp),
					color = MaterialTheme.colorScheme.surfaceContainerLowest
				)
			}else {
				Text(text = stringResource(id = R.string.apply))
			}
		}

	}
}