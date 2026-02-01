package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.PageDto
import com.example.deniseshop.core.domain.model.Page

fun PageDto.toPage() = Page(
	id = id,
	name = name,
	image = image,
	description = description,
	content = content
)