package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.AddressDto
import com.example.deniseshop.core.domain.model.Address
import com.example.deniseshop.core.domain.model.AddressType
import java.util.Locale.getDefault

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
	type = try {
		AddressType.valueOf(
			value = type.replaceFirstChar {
				if (it.isLowerCase())
					it.titlecase(getDefault())
				else
					it.toString()
			}
		)
	} catch (_: Exception){
		AddressType.Other
	},
	default = default
)