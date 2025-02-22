package com.example.deniseshop.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.ui.theme.Orange100
import com.example.deniseshop.ui.models.UiReview
import com.example.deniseshop.ui.models.UiReviewStat

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
				Row (verticalAlignment = Alignment.CenterVertically){
					RatingStar(
						rating = star.toInt(),
						starModifier = Modifier.size(14.dp),
						startTint = Orange100
					)
					Spacer(Modifier.width(4.dp))
					LinearProgressIndicator(
						progress = { ( reviewCount.toFloat() / reviewStat.totalReview.toFloat()) },
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

@Composable
fun ReviewItem(
	modifier: Modifier = Modifier,
	review: UiReview
){
	Column (modifier = modifier){
		Row (
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.Top
		){
			Row(
				verticalAlignment = Alignment.Top
			) {
				Image(
					painter = rememberAsyncImagePainter(model = review.reviewerImage),
					contentDescription ="",
					modifier = Modifier
						.width(32.dp)
						.height(32.dp)
						.clip(CircleShape)
						.background(color = MaterialTheme.colorScheme.surfaceContainer, shape = CircleShape),
					contentScale = ContentScale.Crop
				)
				Spacer(Modifier.width(8.dp))
				Column {
					Text(
						text = review.reviewerName,
						style = MaterialTheme.typography.bodyMedium.copy(
							fontWeight = FontWeight.W500
						)
					)
					RatingStar(
						rating = 4,
						starModifier = Modifier.size(14.dp)
					)
				}
			}
			Text(
				text = review.createdAt.substring(0,10),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
				)
			)
		}
		Spacer(Modifier.height(4.dp))
		Text(
			text = review.comment,
			style = MaterialTheme.typography.bodyMedium.copy(
				color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
			)
		)
	}
}