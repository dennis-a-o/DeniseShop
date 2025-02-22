package com.example.deniseshop.ui.components.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.common.event.ProductFilterEvent
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.ui.components.RatingStar
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.models.ProductFilterState
import com.example.deniseshop.ui.models.UiProductFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFilter(
	filterOptionState: ScreenState<UiProductFilter>,
	filterState: ProductFilterState,
	onFilterEvent: (ProductFilterEvent) -> Unit,
	onClose: () -> Unit
) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.filterBy)) },
				modifier = Modifier.shadow(0.5.dp),
				navigationIcon = {
					IconButton(onClick = { onClose() }) {
						Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "" )
					}
				}
			)
		},
		bottomBar = {
			if(filterOptionState is ScreenState.Success) {
				FilterDialogBottomBar(
					onReset = { onFilterEvent(ProductFilterEvent.Reset) },
					onApply = { onFilterEvent(ProductFilterEvent.Apply) }
				)
			}
		}
	){ paddingValues ->
		Box(modifier = Modifier.padding(paddingValues)) {
			when(filterOptionState){
				is ScreenState.Error -> {
					ErrorUi(onErrorAction = { onClose() })
				}
				is ScreenState.Loading -> {
					LoadingUi()
				}
				is ScreenState.Success -> {
					ProductFilter(
						filterData = filterOptionState.uiData,
						filterState = filterState,
						onFilterEvent = { onFilterEvent(it) }
					)
				}
			}
		}
	}
}

@Composable
private fun ProductFilter(
	filterData: UiProductFilter,
	filterState: ProductFilterState,
	onFilterEvent: (ProductFilterEvent) -> Unit
){
	LazyColumn{
		filterData.categories?.let { categories ->
			if(categories.isNotEmpty()) {
				item {
					FilterSection(
						title = stringResource(R.string.categories),
						options = categories,
						selectedOptions = filterState.selectedCategories,
						onOptionToggle = { onFilterEvent(ProductFilterEvent.CategoryChanged(it)) }
					)
				}
			}
		}
		filterData.brands?.let { brands ->
			if(brands.isNotEmpty()) {
				item {
					FilterSection(
						title = stringResource(R.string.brands),
						options = brands,
						selectedOptions = filterState.selectedBrands,
						onOptionToggle = { onFilterEvent(ProductFilterEvent.BrandChanged(it)) }
					)
				}
			}
		}
		item {
			FilterPriceSection(
				maxPrice = filterData.maxPrice?: 100000L,
				currency = filterData.currency?: "",
				priceRange = filterState.priceRange,
				onFilterEvent = { onFilterEvent(it) }
			)
		}
		filterData.colors?.let { colors ->
			if (colors.isNotEmpty()) {
				item {
					FilterSection(
						title = stringResource(R.string.colors),
						options = colors,
						selectedOptions = filterState.selectedColors,
						onOptionToggle = { onFilterEvent(ProductFilterEvent.ColorChanged(it)) }
					)
				}
			}
		}
		filterData.sizes?.let { sizes ->
			if (sizes.isNotEmpty()) {
				item {
					FilterSection(
						title = stringResource(R.string.sizes),
						options = sizes,
						selectedOptions = filterState.selectedSize,
						onOptionToggle = { onFilterEvent(ProductFilterEvent.SizeChanged(it)) }
					)
				}
			}
		}
		item {
			FilterRatingSection (
				onFilterEvent = { onFilterEvent(it) },
				ratingState = filterState.rating
			)
		}
	}

}

@Composable
private fun FilterSection(
	title: String,
	options: List<String>,
	selectedOptions: List<String>,
	onOptionToggle: (String) -> Unit
){
	Column {
		Spacer(Modifier.height(8.dp))
		Text(
			text = title,
			modifier = Modifier.padding(horizontal = 16.dp),
			style = MaterialTheme.typography.titleMedium
		)
		Spacer(Modifier.height(8.dp))
		LazyRow {
			item { Spacer(Modifier.width(16.dp)) }
			options.forEach { option ->
				item {
					ElevatedFilterChip(
						selected = option in selectedOptions,
						onClick = { onOptionToggle(option) },
						label = { Text(text = option) },
						modifier = Modifier.padding(end = 8.dp),
						shape = RoundedCornerShape(16.dp)
					)
				}
			}
			item { Spacer(Modifier.width(8.dp)) }
		}
	}
}

@Composable
private fun FilterPriceSection(
	maxPrice: Long,
	priceRange: ClosedFloatingPointRange<Float>,
	currency: String,
	onFilterEvent: (ProductFilterEvent) -> Unit
) {
	val rangeStart = "%.2f".format(priceRange.start)
	val rangeEnd = "%.2f".format(priceRange.endInclusive)

	Column(modifier = Modifier.padding(horizontal = 16.dp)) {
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
				onFilterEvent(ProductFilterEvent.PriceRangeChanged(it))
			},
			valueRange = 0f..maxPrice.toFloat()
		)
	}
}

@Composable
private fun FilterRatingSection(
	onFilterEvent: (ProductFilterEvent) -> Unit,
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
				onFilterEvent(ProductFilterEvent.RatingChanged(it))
			},
			isIndicator = true,
			starModifier = Modifier
				.size(36.dp)
				.padding(end = 8.dp),
		)
	}
}

@Composable
private fun FilterDialogBottomBar(
	modifier: Modifier = Modifier,
	onReset: () -> Unit,
	onApply: () -> Unit,
){
	Row(
		modifier = modifier
			.shadow(0.5.dp)
			.padding(8.dp)
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceAround,
		verticalAlignment = Alignment.CenterVertically
	) {
		OutlinedButton(
			onClick = { onReset() },
			modifier = Modifier.fillMaxWidth(fraction = 0.5f),
			shape = RoundedCornerShape(16.dp)
		) {
			Text(text = stringResource(R.string.reset))
		}
		Spacer(Modifier.width(8.dp))
		Button(
			onClick = { onApply() },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(16.dp)
		) {
			Text(text = stringResource(R.string.apply))
		}
	}
}