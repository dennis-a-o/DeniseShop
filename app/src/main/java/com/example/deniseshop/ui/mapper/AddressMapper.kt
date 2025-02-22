package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiAddress
import com.example.deniseshop.ui.models.UiAddress
import javax.inject.Inject

class AddressApiToUiMapper @Inject constructor(): BaseMapper<ApiAddress, UiAddress> {
	override fun map(input: ApiAddress): UiAddress {
		return UiAddress(
			id = input.id,
			useId = input.useId,
			name = input.name,
			email = input.email,
			phone = input.phone,
			country = input.country,
			state = input.state,
			city = input.city,
			address = input.address,
			zipCode = input.zipCode,
			type = input.type,
			default = input.default
		)
	}
}

class AddressListApiToUiMapper @Inject constructor(): BaseListMapper<ApiAddress, UiAddress>{
	override fun map(input: List<ApiAddress>): List<UiAddress> {
		return input.map {
			UiAddress(
				id = it.id,
				useId = it.useId,
				name = it.name,
				email = it.email,
				phone = it.phone,
				country = it.country,
				state = it.state,
				city = it.city,
				address = it.address,
				zipCode = it.zipCode,
				type = it.type,
				default = it.default
			)
		}
	}
}