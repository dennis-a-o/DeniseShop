package com.example.deniseshop.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IconWithBadge(
	badge: Int,
	icon: Int,
	modifier: Modifier,
	tint: Color = LocalContentColor.current,
){
	Box (modifier = Modifier.size(32.dp)){
		Icon(
			painter = painterResource(id = icon),
			modifier = modifier.align(
				Alignment.BottomCenter,
			),
			contentDescription = null,
			tint = tint
		)

		if (badge != 0) {
			Text(
				text = "$badge",
				textAlign = TextAlign.Center,
				fontSize = 12.sp,
				lineHeight = 16.sp,
				color = MaterialTheme.colorScheme.surface,
				modifier = Modifier
					.padding(top = 4.dp)
					.clip(CircleShape)
					.background(MaterialTheme.colorScheme.primary)
					.align(Alignment.TopEnd)
					.size(16.dp),
			)
		}
	}
}