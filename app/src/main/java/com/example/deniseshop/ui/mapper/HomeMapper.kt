package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiBrand
import com.example.deniseshop.data.models.ApiCategory
import com.example.deniseshop.data.models.ApiFeaturedFlashSale
import com.example.deniseshop.data.models.ApiFlashSale
import com.example.deniseshop.data.models.ApiHome
import com.example.deniseshop.data.models.ApiProduct
import com.example.deniseshop.data.models.ApiSlider
import com.example.deniseshop.ui.models.UiBrand
import com.example.deniseshop.ui.models.UiCategory
import com.example.deniseshop.ui.models.UiFeaturedFlashSale
import com.example.deniseshop.ui.models.UiFlashSale
import com.example.deniseshop.ui.models.UiHome
import com.example.deniseshop.ui.models.UiProduct
import com.example.deniseshop.ui.models.UiSlider
import javax.inject.Inject

class HomeApiToUiMapper @Inject constructor(
	private val categoryListApiToUiMapper: BaseListMapper<ApiCategory, UiCategory>,
	private val  productListApiToUiMapper: BaseListMapper<ApiProduct, UiProduct>,
	private val brandListApiToUiMapper: BaseListMapper<ApiBrand, UiBrand>,
	private val featuredFlashSaleApiToUiMapper: BaseMapper<ApiFeaturedFlashSale, UiFeaturedFlashSale>,
	private val sliderListApiToUiMapper: BaseListMapper<ApiSlider, UiSlider>
): BaseMapper<ApiHome, UiHome> {
	override fun map(input: ApiHome): UiHome {
		return UiHome(
			sliders = sliderListApiToUiMapper.map(input.sliders),
			categories = categoryListApiToUiMapper.map(input.categories),
			featuredFlashSale = input.featuredFlashSale?.let { featuredFlashSaleApiToUiMapper.map(it) },
			featured = productListApiToUiMapper.map(input.featured) ,
			brands =  brandListApiToUiMapper.map(input.brands) ,
			recentViewed =  productListApiToUiMapper.map(input.recentViewed),
			newArrival = productListApiToUiMapper.map(input.newArrival)
		)
	}


}