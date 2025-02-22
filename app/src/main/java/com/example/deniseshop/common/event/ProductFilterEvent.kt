package com.example.deniseshop.common.event

sealed class ProductFilterEvent {
	class PriceRangeChanged(val priceRange: ClosedFloatingPointRange<Float>): ProductFilterEvent()
	class CategoryChanged(val category: String): ProductFilterEvent()
	class BrandChanged(val brand: String): ProductFilterEvent()
	class ColorChanged(val color: String): ProductFilterEvent()
	class SizeChanged(val size: String): ProductFilterEvent()
	class RatingChanged(val rating: Int): ProductFilterEvent()
	data object Close: ProductFilterEvent()
	data object Open: ProductFilterEvent()
	data object Reset : ProductFilterEvent()
	data object Apply : ProductFilterEvent()
}