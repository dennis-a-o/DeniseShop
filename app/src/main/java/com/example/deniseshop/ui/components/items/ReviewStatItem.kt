package com.example.deniseshop.ui.components.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deniseshop.ui.components.RatingStar
import com.example.deniseshop.ui.models.UiReviewStat
import com.example.deniseshop.ui.theme.Orange100

@Composable
fun ReviewStatItem(
	modifier: Modifier = Modifier,
	reviewStat: UiReviewStat
){
	Row(
		modifier = modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Column {
			Text(
				text = reviewStat.averageRating.toString(),
				style = MaterialTheme.typography.headlineLarge
			)
			Text(
				text = "${reviewStat.totalReview} ratings",
				style = MaterialTheme.typography.bodySmall.copy(
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
				),
			)
		}
		Spacer(Modifier.width(4.dp))
		Column {
			reviewStat.starCount.forEach { (star, reviewCount) ->
				Row(verticalAlignment = Alignment.CenterVertically) {
					RatingStar(
						rating = star.toInt(),
						starModifier = Modifier.size(14.dp),
						startTint = Orange100
					)
					Spacer(Modifier.width(4.dp))
					LinearProgressIndicator(
						progress = { (reviewCount.toFloat() / reviewStat.totalReview.toFloat()) },
						modifier = Modifier.fillMaxWidth(fraction = 0.9f),
						gapSize = 0.dp
					)
					Spacer(Modifier.width(4.dp))
					Text(
						text = reviewCount.toString(),
						style = MaterialTheme.typography.bodySmall
					)
				}
				Spacer(Modifier.height(4.dp))
			}
		}
	}
}