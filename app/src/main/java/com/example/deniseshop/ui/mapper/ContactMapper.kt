package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiContact
import com.example.deniseshop.ui.models.UiContact
import javax.inject.Inject

class ContactListApiToUiMapper @Inject constructor(): BaseListMapper<ApiContact, UiContact> {
	override fun map(input: List<ApiContact>): List<UiContact> {
		return input.map {
			UiContact(
				contact = it.contact,
				type = it.type,
				description = it.description
			)
		}
	}
}