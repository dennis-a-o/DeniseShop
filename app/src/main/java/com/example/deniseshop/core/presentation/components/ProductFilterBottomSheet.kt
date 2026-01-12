package com.example.deniseshop.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.ProductFilter
import com.example.deniseshop.core.presentation.models.ProductFilterState
import com.example.deniseshop.ui.components.RatingStar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFilterBottomSheet(
	filter: ProductFilter,
	onApplyFilterState: (ProductFilterState) -> Unit,
	onDismiss: () -> Unit
) {
	var filterState by remember { mutableStateOf(ProductFilterState()) }

	ModalBottomSheet(
		onDismissRequest = onDismiss,
		sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
		) {
			TopAppBar(
				title = { Text(text = stringResource(R.string.filterBy)) },
				modifier = Modifier.shadow(0.5.dp),
				navigationIcon = {
					IconButton(onClick = onDismiss ) {
						Icon(
							painter = painterResource(id = R.drawable.ic_arrow_back),
							contentDescription = ""
						)
					}
				}
			)
			LazyColumn{
				filter.categories.let { categories ->
					if(categories.isNotEmpty()) {
						item {
							FilterSection(
								title = stringResource(R.string.categories),
								options = categories,
								selectedOptions = filterState.selectedCategories,
								onOptionClick = {
									filterState = if(it in filterState.selectedCategories){
										filterState.copy(
											selectedCategories = filterState.selectedCategories - it
										)
									}else{
										filterState.copy(
											selectedCategories = filterState.selectedCategories + it
										)
									}
								}
							)
						}
					}
				}
				filter.brands.let { brands ->
					if(brands.isNotEmpty()) {
						item {
							FilterSection(
								title = stringResource(R.string.brands),
								options = brands,
								selectedOptions = filterState.selectedBrands,
								onOptionClick = {
									filterState = if(it in filterState.selectedBrands){
										filterState.copy(
											selectedBrands = filterState.selectedBrands - it
										)
									}else{
										filterState.copy(
											selectedBrands = filterState.selectedBrands + it
										)
									}
								}
							)
						}
					}
				}
				item {
					FilterPriceSection(
						maxPrice = filter.maxPrice,
						currency = filter.currency,
						priceRange = filterState.priceRange,
						onChange = {
							filterState = filterState.copy(priceRange = it)
						}
					)
				}
				filter.colors.let { colors ->
					if (colors.isNotEmpty()) {
						item {
							FilterSection(
								title = stringResource(R.string.colors),
								options = colors,
								selectedOptions = filterState.selectedColors,
								onOptionClick = {
									filterState = if(it in filterState.selectedColors){
										filterState.copy(
											selectedColors = filterState.selectedColors - it
										)
									}else{
										filterState.copy(
											selectedColors = filterState.selectedColors + it
										)
									}
								}
							)
						}
					}
				}
				filter.sizes.let { sizes ->
					if (sizes.isNotEmpty()) {
						item {
							FilterSection(
								title = stringResource(R.string.sizes),
								options = sizes,
								selectedOptions = filterState.selectedSize,
								onOptionClick = {
									filterState = if(it in filterState.selectedSize){
										filterState.copy(
											selectedSize = filterState.selectedSize - it
										)
									}else{
										filterState.copy(
											selectedSize = filterState.selectedSize + it
										)
									}
								}
							)
						}
					}
				}
				item {
					FilterRatingSection (
						ratingState = filterState.rating,
						onChange = {
							filterState = filterState.copy(rating = it)
						}
					)
				}
			}
			BottomAppBar() {
				OutlinedButton(
					onClick = {
						filterState = ProductFilterState()
							.copy(priceRange = 0f..filter.maxPrice.toFloat())
					},
					modifier = Modifier
						.weight(50f),
					shape = RoundedCornerShape(16.dp)
				) {
					Text(text = stringResource(R.string.reset))
				}
				Spacer(Modifier.width(8.dp))
				Button(
					onClick = {
						onApplyFilterState(filterState)
						onDismiss()
					},
					modifier = Modifier
						.weight(50f),
					shape = RoundedCornerShape(16.dp)
				) {
					Text(text = stringResource(R.string.apply))
				}
			}
		}
	}
}


@Composable
private fun FilterSection(
	title: String,
	options: List<String>,
	selectedOptions: List<String>,
	onOptionClick: (String) -> Unit
){
	Column {
		Spacer(Modifier.height(8.dp))
		Text(
			text = title,
			modifier = Modifier.padding(horizontal = 16.dp),
			style = MaterialTheme.typography.titleMedium
		)
		Spacer(Modifier.height(8.dp))
		LazyRow (
			contentPadding = PaddingValues(horizontal = 16.dp),
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		){
			options.forEach { option ->
				item {
					ElevatedFilterChip(
						selected = option in selectedOptions,
						onClick = { onOptionClick(option) },
						label = {
							Text(text = option)
								},
						shape = RoundedCornerShape(16.dp)
					)
				}
			}
		}
	}
}

@Composable
private fun FilterPriceSection(
	maxPrice: Long,
	priceRange: ClosedFloatingPointRange<Float>,
	currency: String,
	onChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
	val rangeStart = "%.2f".format(priceRange.start)
	val rangeEnd = "%.2f".format(priceRange.endInclusive)

	Column(
		modifier = Modifier
			.padding(horizontal = 16.dp)
	) {
		Spacer(Modifier.height(8.dp))
		Text(
			text = stringResource(R.string.priceRange),
			style = MaterialTheme.typography.titleMedium
		)
		Spacer(Modifier.height(8.dp))
		Row (
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		){
			Text(
				text = "$currency$rangeStart",
				style = MaterialTheme.typography.bodyMedium
			)
			Text(
				text = "$currency$rangeEnd",
				style = MaterialTheme.typography.bodyMedium
			)
		}

		RangeSlider(
			value = priceRange,
			onValueChange = {
				onChange(it)
			},
			valueRange = 0f..maxPrice.toFloat()
		)
	}
}

@Composable
private fun FilterRatingSection(
	onChange: (Int) -> Unit,
	ratingState: Int,
){
	Column (modifier = Modifier.padding(horizontal = 16.dp)){
		Spacer(Modifier.height(8.dp))
		Text(
			text = stringResource(R.string.rating),
			style = MaterialTheme.typography.titleMedium
		)
		Text(
			text = "$ratingState stars and above",
			style = MaterialTheme.typography.bodySmall.copy(
				color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
			)
		)
		RatingStar(
			rating = ratingState,
			onStartClick = {
				onChange(it)
			},
			isIndicator = true,
			starModifier = Modifier
				.size(36.dp)
				.padding(end = 8.dp),
		)
	}
}

