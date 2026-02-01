package com.example.deniseshop.feature.categoryproducts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deniseshop.core.domain.model.Category
import com.example.deniseshop.core.presentation.components.CategoryItem

@Composable
fun CategoriesSection(
	categories: List<Category>,
	onCategoryClick: (Long) -> Unit,
	modifier: Modifier = Modifier
) {
	Column (
		modifier = modifier
	){
		LazyRow (
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		){
			categories.forEach{ category ->
				item {
					CategoryItem(
						category = category,
						modifier = Modifier
							.width(80.dp),
						onClick = { onCategoryClick(it) }
					)
				}
			}
		}
	}
}