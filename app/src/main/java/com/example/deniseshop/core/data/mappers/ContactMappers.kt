package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.ContactDto
import com.example.deniseshop.core.domain.model.Contact

fun ContactDto.toContact() = Contact(
	contact = contact,
	type = type,
	description = description
)