package com.example.deniseshop.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LazyRowGrid(
	modifier: Modifier = Modifier,
	rows: Int,
	itemCount: Int,
	gridWidth: Dp = Dp.Unspecified,
	gridWidthSpacing: Dp = 0.dp,
	gridHeightSpacing: Dp = 0.dp,
	gridHorizontalPadding: Dp = 0.dp,
	gridVerticalPadding: Dp = 0.dp,
	content: @Composable (Int) -> Unit
){
	LazyRow (modifier = modifier){
		var columns = itemCount/rows
		if (columns == 0) columns = 1//privent division by zero error
		if(itemCount.mod(columns) > 0){
			columns += 1
		}

		for(columnIndex in 0 until columns) {
			val firstIndex = columnIndex * rows

			if (columnIndex == 0)
				item { Spacer(Modifier.width(gridHorizontalPadding)) }

			item {
				Column (modifier = Modifier.width(gridWidth)) {
					Spacer(Modifier.width(gridVerticalPadding))
					for (rowIndex in 0 until rows) {
						val index = firstIndex + rowIndex
						if (index < itemCount) {
							content(index)
						}
						if (rowIndex < (rows - 1))
							Spacer(Modifier.height(gridHeightSpacing))
					}
					Spacer(Modifier.width(gridVerticalPadding))
				}
				if (columnIndex < (columns - 1))
					Spacer(Modifier.width(gridWidthSpacing))
			}
			if (columnIndex == (columns - 1))
				item { Spacer(Modifier.width(gridHorizontalPadding)) }
		}
	}
}

@Composable
fun ColumnGrid(
	modifier: Modifier = Modifier,
	columns: Int,
	itemCount: Int,
	gridWidthSpacing: Dp = 0.dp,
	gridHeightSpacing: Dp = 0.dp,
	content: @Composable (Int) -> Unit
){
	Column (modifier = modifier){
		var rows = (itemCount / columns)
		if (itemCount.mod(columns) > 0){
			rows += 1
		}

		for (rowIndex in 0 until rows){
			val firstIndex = rowIndex * columns

			Row {
				for (columnIndex in 0 until columns){
					val index = firstIndex + columnIndex
					Box (
						Modifier
							.fillMaxWidth()
							.weight(1f)){
						if (index < itemCount){
							content(index)
						}
					}
					if ( columnIndex < (columns -1))
						Spacer(Modifier.width(gridWidthSpacing))
				}
			}
			if (rowIndex < (rows -1))
				Spacer(Modifier.height(gridHeightSpacing))
		}
	}
}