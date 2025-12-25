package com.example.deniseshop.ui.components.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.ui.models.UiCategory

@Composable
fun CategoryItem(
	modifier: Modifier = Modifier,
	category: UiCategory,
	onClick: (Long) -> Unit
){
	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable { onClick(category.id) }
	){
		Image(
			painter = rememberAsyncImagePainter(model = category.image),
			contentDescription = "",
			modifier = Modifier
				.fillMaxWidth()
				.height(50.dp),
			contentScale = ContentScale.Fit
		)
		Text(
			text = category.name,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 4.dp),
			style = MaterialTheme.typography.bodySmall,
			textAlign = TextAlign.Center,
			maxLines = 2,
			overflow = TextOverflow.Ellipsis
		)
		Spacer(Modifier.height(4.dp))
	}
}