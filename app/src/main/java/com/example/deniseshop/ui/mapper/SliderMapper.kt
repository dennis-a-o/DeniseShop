package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiSlider
import com.example.deniseshop.ui.models.UiSlider
import javax.inject.Inject

class SliderListApiToUiMapper @Inject constructor(): BaseListMapper<ApiSlider, UiSlider> {
	override fun map(input: List<ApiSlider>): List<UiSlider> {
		return input.map { apiSlider ->
			UiSlider(
				id = apiSlider.id,
				title = apiSlider.title,
				subTitle = apiSlider.subTitle,
				highlightText = apiSlider.highlightText,
				image = apiSlider.image,
				description = apiSlider.description,
				type = apiSlider.type,
				typeId = apiSlider.typeId,
				link = apiSlider.link,
				buttonText = apiSlider.buttonText
			)
		}
	}
}