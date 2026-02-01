package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.PaymentMethodDto
import com.example.deniseshop.core.domain.model.PaymentMethod

fun PaymentMethodDto.toPaymentMethod() = PaymentMethod(
	id = id,
	name = name,
	slug = slug,
	logo = logo
)
