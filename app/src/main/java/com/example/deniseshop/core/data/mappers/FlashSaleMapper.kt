package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.FeaturedFlashSaleDto
import com.example.deniseshop.core.data.dto.FlashSaleDto
import com.example.deniseshop.core.domain.model.FeaturedFlashSale
import com.example.deniseshop.core.domain.model.FlashSale

fun FlashSaleDto.toFlashSale(): FlashSale{
	return FlashSale(
		id = id,
		name = name,
		image = image,
		description = description,
		endDate = endDate
	)
}

fun  FeaturedFlashSaleDto.toFeaturedFlashSale(): FeaturedFlashSale{
	return FeaturedFlashSale(
		flashSale = flashSale.toFlashSale(),
		products = products.map { it.toProduct() }
	)
}