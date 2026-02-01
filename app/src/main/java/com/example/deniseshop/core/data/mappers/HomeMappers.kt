package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.HomeDto
import com.example.deniseshop.core.data.dto.SliderDto
import com.example.deniseshop.core.domain.model.Home
import com.example.deniseshop.core.domain.model.Slider

fun HomeDto.toHome(): Home {
	return Home(
		sliders = sliders.map { it.toSlider() },
		categories = categories.map { it.toCategory() },
		featuredFlashSale = featuredFlashSale?.toFeaturedFlashSale(),
		featured = featured.map { it.toProduct() },
		brands = brands.map { it.toBrand() },
		recentViewed = recentViewed.map { it.toProduct() },
		newArrival = newArrival.map { it.toProduct() }
	)
}


fun SliderDto.toSlider() = Slider(
	id = id,
	title = title,
	subTitle = subTitle,
	image = image,
	highlightText = highlightText,
	description = description,
	link = link,
	type = type,
	typeId = typeId,
	buttonText = buttonText
)