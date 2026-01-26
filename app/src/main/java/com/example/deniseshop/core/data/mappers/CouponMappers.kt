package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.CouponDto
import com.example.deniseshop.core.domain.model.Coupon

fun CouponDto.toCoupon() = Coupon(
	id = id,
	code = code,
	value = value,
	type = type,
	description = description
)