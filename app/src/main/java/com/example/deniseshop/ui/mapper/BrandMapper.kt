package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiBrand
import com.example.deniseshop.ui.models.UiBrand
import javax.inject.Inject

class BrandApiToUiMapper @Inject constructor(): BaseMapper<ApiBrand, UiBrand> {
	override fun map(input: ApiBrand): UiBrand {
		return UiBrand(
			id = input.id,
			name = input.name,
			logo = input.logo
		)
	}
}

class BrandListApiToUiMapper @Inject constructor(): BaseListMapper<ApiBrand, UiBrand> {
	override fun map(input: List<ApiBrand>): List<UiBrand> {
		return input.map {
			UiBrand(
				id = it.id,
				name = it.name,
				logo = it.logo
			)
		}
	}
}