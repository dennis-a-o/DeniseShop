package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.FaqDto
import com.example.deniseshop.core.domain.model.Faq

fun FaqDto.toFaq() = Faq(
	id = id,
	question = question,
	answer = answer
)