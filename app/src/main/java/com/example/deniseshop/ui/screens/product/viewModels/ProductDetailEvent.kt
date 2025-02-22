package com.example.deniseshop.ui.screens.product.viewModels

sealed class ProductDetailEvent {
	class SelectColor(val color: String):  ProductDetailEvent()
	class SelectSize(val size: String): ProductDetailEvent()
	class ProductDescriptionVisible(val isVisible: Boolean) : ProductDetailEvent()
	class AddQuantity(val quantity: Int): ProductDetailEvent()
	class MinusQuantity(val quantity: Int): ProductDetailEvent()
}