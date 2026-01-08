package com.example.deniseshop.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.deniseshop.R

@Composable
fun RatingStar(
	modifier: Modifier = Modifier,
	rating: Int = 0,
	maxRating: Int = 5,
	onStartClick: (Int) -> Unit = {},
	isIndicator: Boolean = false,
	starModifier: Modifier = Modifier,
	startTint: Color = MaterialTheme.colorScheme.primary
){
	Row (modifier = modifier){
		for (i in 1..maxRating){
			if (i <= rating){
				Icon(
					painter = painterResource(R.drawable.ic_start_filled),
					contentDescription = null,
					modifier = starModifier.clickable(isIndicator) { onStartClick(i) },
					tint = startTint,
				)
			}else{
				Icon(
					painter = painterResource(R.drawable.ic_start_outline),
					contentDescription = null,
					modifier = starModifier.clickable(isIndicator) { onStartClick(i) },
					tint = Color.Gray,
				)
			}
		}
	}
}