package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.AddressDto
import com.example.deniseshop.core.domain.model.Address

fun AddressDto.toAddress() = Address(
	id = id,
	useId = useId,
	name = name,
	email = email,
	phone = phone,
	country = country,
	city = city,
	state = state,
	address = address,
	zipCode = zipCode,
	type = type,
	default = default
)