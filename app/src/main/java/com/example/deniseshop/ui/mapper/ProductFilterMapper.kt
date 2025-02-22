package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiProductFilter
import com.example.deniseshop.ui.models.UiProductFilter
import javax.inject.Inject

class ProductFilterApiToUiMapper @Inject constructor(): BaseMapper<ApiProductFilter, UiProductFilter> {
	override fun map(input: ApiProductFilter): UiProductFilter {
		return UiProductFilter(
			categories = input.categories,
			brands = input.brands,
			maxPrice = input.maxPrice,
			colors = input.colors,
			sizes = input.sizes,
			currency = input.currency
		)
	}
}