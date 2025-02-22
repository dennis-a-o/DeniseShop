package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiPage
import com.example.deniseshop.ui.models.UiPage
import javax.inject.Inject

class PageApiToUiMapper @Inject constructor(): BaseMapper<ApiPage, UiPage> {
	override fun map(input: ApiPage): UiPage {
		return UiPage(
			id = input.id,
			name = input.name,
			image = input.image,
			description = input.description,
			content = input.content
		)
	}
}