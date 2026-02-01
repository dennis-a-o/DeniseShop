package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.BrandDto
import com.example.deniseshop.core.domain.model.Brand

fun BrandDto.toBrand() = Brand(
	id = id,
	name = name,
	logo = logo
)