package com.example.deniseshop.feature.categories.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.deniseshop.core.domain.model.Brand

@Composable
fun CategoriesBrandItem(
	brand: Brand,
	onClick: (Long) -> Unit,
	modifier: Modifier = Modifier,
){
	Column (
		modifier = modifier
			.clip(RoundedCornerShape(16.dp))
			.clickable {
				onClick(brand.id)
			}
			.padding(8.dp),
		verticalArrangement = Arrangement.Center
	){
		AsyncImage(
			model = brand.logo,
			contentDescription = "",
			modifier = Modifier
				.clip(RoundedCornerShape(14.dp))
				.height(50.dp)
				.fillMaxWidth(),
			contentScale = ContentScale.Fit
		)
		Text(
			text = brand.name,
			style = MaterialTheme.typography.bodySmall,
			maxLines = 2,
			textAlign = TextAlign.Center
		)
	}
}