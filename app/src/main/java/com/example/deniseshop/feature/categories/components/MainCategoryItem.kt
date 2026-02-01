package com.example.deniseshop.feature.categories.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.deniseshop.core.domain.model.Category

@Composable
fun MainCategoryItem(
	category: Category,
	onClick: (Category) -> Unit,
	selected: Boolean,
	modifier: Modifier = Modifier
){
	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(
				color = if (selected){
					MaterialTheme.colorScheme.surface
				}else{
					MaterialTheme.colorScheme.surfaceContainerLowest
				}
			)
			.clickable {
				onClick(category)
			}
			.padding(8.dp)
			.fillMaxWidth(),
		verticalArrangement = Arrangement.Center
	){
		AsyncImage(
			model = category.image,
			contentDescription = "" ,
			modifier = Modifier
				.height(50.dp)
				.clip(RoundedCornerShape(16.dp)),
			contentScale = ContentScale.Crop
		)
		Text(
			text = category.name,
			style = MaterialTheme.typography.bodyMedium,
			overflow = TextOverflow.Ellipsis,
			maxLines = 2,
			textAlign = TextAlign.Center,
			fontWeight = if (selected){
				FontWeight.Bold
			}else{
				FontWeight.Normal
			}
		)
	}
}