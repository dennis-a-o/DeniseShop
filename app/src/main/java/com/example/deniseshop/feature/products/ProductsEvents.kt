package com.example.deniseshop.feature.products

import com.example.deniseshop.core.domain.model.ProductSortOption
import com.example.deniseshop.core.presentation.models.ProductFilterState

sealed class ProductsEvent{
	data class SortOptionChange(val sortOption: ProductSortOption): ProductsEvent()
	data class WishlistToggle(val productId: Long): ProductsEvent()
	data class CartToggle(val productId: Long): ProductsEvent()
	data class ProductFilterStateChange(val filterState: ProductFilterState): ProductsEvent()
}