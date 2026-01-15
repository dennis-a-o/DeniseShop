package com.example.deniseshop.feature.brandproducts

import com.example.deniseshop.core.domain.model.Brand
import com.example.deniseshop.core.domain.model.ProductFilter
import com.example.deniseshop.core.domain.model.ProductSortOption
import com.example.deniseshop.core.presentation.models.ProductFilterState

data class BrandProductsState(
	val brand: Brand? = null,
	val brands: List<Brand> = emptyList(),
	val filter: ProductFilter = ProductFilter(),
	val filterState: ProductFilterState = ProductFilterState(),
	val sortOption: ProductSortOption = ProductSortOption.DATE_ASCENDING,
)
