package com.example.deniseshop.feature.categoryproducts

import com.example.deniseshop.core.domain.model.Category
import com.example.deniseshop.core.domain.model.ProductFilter
import com.example.deniseshop.core.domain.model.ProductSortOption
import com.example.deniseshop.core.presentation.models.ProductFilterState

data class CategoryProductsState(
	val category: Category? = null,
	val filter: ProductFilter = ProductFilter(),
	val filterState: ProductFilterState = ProductFilterState(),
	val sortOption: ProductSortOption = ProductSortOption.DATE_ASCENDING,
)
