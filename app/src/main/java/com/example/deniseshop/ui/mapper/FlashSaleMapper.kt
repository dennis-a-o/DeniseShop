package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiFeaturedFlashSale
import com.example.deniseshop.data.models.ApiFlashSale
import com.example.deniseshop.data.models.ApiProduct
import com.example.deniseshop.ui.models.UiFeaturedFlashSale
import com.example.deniseshop.ui.models.UiFlashSale
import com.example.deniseshop.ui.models.UiProduct
import javax.inject.Inject

class FlashSaleApiToUiMapper @Inject constructor(): BaseMapper<ApiFlashSale, UiFlashSale> {
	override fun map(input: ApiFlashSale): UiFlashSale {
		return UiFlashSale(
			id = input.id,
			name = input.name,
			image = input.image,
			description = input.description,
			endDate = input.endDate
		)
	}
}

class FeaturedFlashSaleApiToUiMapper @Inject constructor(
	private val productListApiToUiMapper: BaseListMapper<ApiProduct, UiProduct>,
	private val flashSaleApiToUiMapper: BaseMapper<ApiFlashSale, UiFlashSale>
):BaseMapper<ApiFeaturedFlashSale, UiFeaturedFlashSale> {
	override fun map(input: ApiFeaturedFlashSale): UiFeaturedFlashSale {
		return  UiFeaturedFlashSale(
			flashSale = flashSaleApiToUiMapper.map(input.flashSale),
			products = productListApiToUiMapper.map(input.products)
		)
	}

}