package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.CategoryDto
import com.example.deniseshop.core.domain.model.Category

fun CategoryDto.toCategory(): Category{
	return Category(
		id = id,
		parentId = parentId,
		name = name,
		image = image,
		icon = icon,
		categories = categories?.map { it.toCategory() },
		brands = brands?.map { it.toBrand() }
	)
}