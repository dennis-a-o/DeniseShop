package com.example.deniseshop.feature.productdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R

@Composable
fun ProductDetailBottomBar(
	isInCart: Boolean,
	onAddToCart: () -> Unit,
	onHomeClick: () -> Unit,
	onViewCartClick: () -> Unit,
	modifier: Modifier = Modifier,
){
	Row (
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 8.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceAround
	){
		OutlinedButton(
			onClick = onHomeClick,
			shape = RoundedCornerShape(16.dp),
			modifier = Modifier
				.weight(30f)
		) {
			Icon(
				painter = painterResource(R.drawable.ic_home_outline) ,
				contentDescription = "",
				modifier = Modifier
					.size(20.dp)
			)
		}
		Spacer(Modifier.width(16.dp))
		if(isInCart){
			Button(
				onClick = onViewCartClick,
				shape = RoundedCornerShape(16.dp),
				modifier = Modifier
					.weight(70f)
			) {
				Text(text = stringResource(R.string.viewInCart))
			}
		}else {
			Button(
				onClick = onAddToCart,
				shape = RoundedCornerShape(16.dp),
				modifier = Modifier
					.weight(70f)
			) {
				Row {
					Icon(
						painter = painterResource(R.drawable.ic_add_shopping_cart),
						contentDescription = "",
						modifier = Modifier
							.size(20.dp)
					)
					Spacer(Modifier.width(8.dp))
					Text(text = stringResource(R.string.addToCart))
				}
			}
		}
	}
}
