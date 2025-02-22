package com.example.deniseshop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R

@Composable
fun OrderSuccessDialog(
	onClickContinue: () -> Unit,
	orderMessage: String
) {
	Column(
		modifier = Modifier
			.background(
				color = MaterialTheme.colorScheme.surfaceContainerLowest,
				shape = RoundedCornerShape(16.dp)
			)
			.padding(vertical = 8.dp, horizontal = 16.dp)
	) {
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_done),
				tint = MaterialTheme.colorScheme.primary,
				contentDescription = ""
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = "Order placed successfully",
				style = MaterialTheme.typography.titleLarge.copy(
					color = MaterialTheme.colorScheme.primary,
					fontWeight = FontWeight.W500
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		Text(
			text = "Thank you for purchasing our product.",
			style = MaterialTheme.typography.bodyLarge
		)

		Spacer(Modifier.height(8.dp))
		Text(
			text = orderMessage,
			style = MaterialTheme.typography.bodyMedium
		)
		Spacer(Modifier.height(8.dp))
		Button(
			onClick = { onClickContinue() },
			shape = RoundedCornerShape(16.dp)
		) {
			Text(text = "Continue shopping")
		}

	}
}