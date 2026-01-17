package com.example.deniseshop.feature.flashsaleproducts

import com.example.deniseshop.core.domain.model.FlashSale
import com.example.deniseshop.core.domain.model.ProductFilter
import com.example.deniseshop.core.domain.model.ProductSortOption
import com.example.deniseshop.core.presentation.models.ProductFilterState

data class FlashSaleProductsState(
	val flashSale: FlashSale? = null,
	val filter: ProductFilter = ProductFilter(),
	val filterState: ProductFilterState = ProductFilterState(),
	val sortOption: ProductSortOption = ProductSortOption.DATE_ASCENDING,
)
